package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.OperationsCenter;
import com.example.demo.entity.SessionRecords;
import com.example.demo.entity.form.CallingForm;
import com.example.demo.mapper.SessionRecordsMapper;
import com.example.demo.service.ISessionRecordsService;
import com.example.demo.utils.KafkaProducer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
@Service
public class SessionRecordsServiceImpl extends ServiceImpl<SessionRecordsMapper, SessionRecords> implements ISessionRecordsService {

    @Resource
    private SessionRecordsMapper sessionRecordsMapper;

    @Resource
    private CacheServiceImpl cacheService;

    @Resource
    private KafkaProducer<String> kafkaProducer;


    /**
     * 呼入处理总线，处理号码排队与通知
     *
     * @param form 呼入信息
     */
    public void callingBus(CallingForm form) {
        // 识别号码是归属与哪些运营中心
        List<OperationsCenter> operationsCenters = sessionRecordsMapper.queryOCByPhone(form.getPhone());
        // 如果没有对应的运营中心则退出
        if (operationsCenters.size() == 0) return;
        for (OperationsCenter operationsCenter : operationsCenters) {
            // 把Session ID写入对应的运营中心
            cacheService.rightPushSessionId(operationsCenter.getOcId(), form.getSessionId());
            // 对MQ下发通知携带运营中心
            kafkaProducer.send(operationsCenter.getOcId(), operationsCenter.getOcId());
        }
    }

    // 接听线程
    // 根据运营中心获取redis中的session id （先进先出原则）
    // 判断该id是否已经挂断
    // 识别是否有空闲坐席

}
