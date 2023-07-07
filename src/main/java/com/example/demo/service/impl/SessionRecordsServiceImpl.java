package com.example.demo.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.OperationsCenter;
import com.example.demo.entity.SessionRecords;
import com.example.demo.entity.form.CallingForm;
import com.example.demo.mapper.SessionRecordsMapper;
import com.example.demo.service.ICache;
import com.example.demo.service.ISessionRecordsService;
import com.example.demo.utils.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
@Service
@Slf4j
public class SessionRecordsServiceImpl extends ServiceImpl<SessionRecordsMapper, SessionRecords> implements ISessionRecordsService {

    @Resource
    private SessionRecordsMapper sessionRecordsMapper;

    @Resource
    private ICache cacheService;

    @Resource
    private KafkaProducer<String> kafkaProducer;

    @Resource
    private AnswerServiceImpl answerService;


    /**
     * 呼入处理总线，处理号码排队与通知
     *
     * @param form 呼入信息
     */
    @Override
    @Async("interaction-thread")
    public void callingBus(CallingForm form) {
        List<OperationsCenter> operationsCenters;
        // 标记会话是否重新进入本运营中心
        boolean flag = false;
        // 判断是否需要转移
        if (form.isTransfer()) {
            // 需要转移的上级运营中心
            OperationsCenter center = sessionRecordsMapper.queryParentOCByOCId(form.getOcId());
            boolean unusedAccount = cacheService.isUnusedAccount(center.getOcId());
            if (!unusedAccount){
                operationsCenters = sessionRecordsMapper.queryOCByOCId(form.getOcId());
                flag = true;
                log.info("上线运营中心暂无坐席，继续在本坐席中心进行等待---{}", JSON.toJSONString(operationsCenters));
            }else {
                operationsCenters = Collections.singletonList(center);
                log.info("转移至上线运营中心列表---{}", JSON.toJSONString(operationsCenters));
            }
        } else {
            // 识别号码是归属与哪些运营中心
            operationsCenters = sessionRecordsMapper.queryOCByPhone(form.getPhone());
            log.info("号码归属于---{}", JSON.toJSONString(operationsCenters));
        }
        // 如果没有对应的运营中心则退出
        if (operationsCenters.size() == 0) {
            log.info("没有对应的运营中心----OC ID:{},Phone:{}", form.getOcId(), form.getPhone());
            return;
        }
        for (OperationsCenter operationsCenter : operationsCenters) {
            log.info("运营中心：{}-接收到Session 会话:{}", operationsCenter.getOcName(), form.getSessionId());
            // 把Session ID写入对应的运营中心
            if (flag) cacheService.leftPushSessionId(operationsCenter.getOcId(), form.getSessionId());
            else cacheService.rightPushSessionId(operationsCenter.getOcId(), form.getSessionId());
            // 对MQ下发通知携带运营中心
            kafkaProducer.send(operationsCenter.getOcId(), operationsCenter.getOcId());
        }
    }

    @KafkaListener(topics = "topic-interaction")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = String.valueOf(kafkaMessage.get());
            log.info("Kafka---------接收消息：Topic-{},Message-{}.Record-{}", topic, message, record);
            answerBus(message);
        }
    }

    /**
     * 接听总线，根据运营中心ID获取会话和坐席，如果有空闲坐席则进行接听，如果没有则进行转发
     *
     * @param ocId 运营中心ID
     */
    public void answerBus(String ocId) {
        // 获取对应运营中心的第一个会话ID
        String sessionId = cacheService.leftPopSessionId(ocId);
        if (StringUtils.isBlank(sessionId)) return;
        log.info("会话ID：{}", sessionId);
        // 判断该会话是否已结束
        boolean isInvalid = cacheService.isMemberInvalidSession(sessionId);
        if (isInvalid) {
            log.info("会话已结束---运营中心：{}，会话：{}", ocId, sessionId);
            return;
        }
        // 获取空闲坐席
        Long unusedAccount = cacheService.getUnusedAccount(ocId);
        log.info("空闲坐席---Account ID:{}", unusedAccount);
        // 如果无坐席则转移至上线
        if (unusedAccount == 0) {
            CallingForm callingForm = new CallingForm().setSessionId(sessionId).setOcId(ocId).setTransfer(true);
            log.info("当前运营中心无空闲坐席，准备转至上线运营中心----当前运营中心：{}", ocId);
            callingBus(callingForm);
            return;
        }
        // 如果有则进行接通
        answerService.answer(sessionId, unusedAccount);
    }

    /**
     * 写入失效的会话
     *
     * @param sessionId 无效会话ID
     */
    @Override
    public boolean invalidSessionList(String sessionId) {
        return cacheService.addInvalidSession(sessionId);
    }

    @Override
    public List<String> getAllPhone() {
        return sessionRecordsMapper.getAllPhone();
    }
}
