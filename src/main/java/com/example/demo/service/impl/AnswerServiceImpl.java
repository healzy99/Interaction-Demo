package com.example.demo.service.impl;

import com.example.demo.entity.SessionRecords;
import com.example.demo.entity.dto.AnswerDTO;
import com.example.demo.entity.enums.SessionHandleState;
import com.example.demo.entity.enums.SessionStatus;
import com.example.demo.mapper.AccWorkTotalStatusMapper;
import com.example.demo.mapper.SessionRecordsMapper;
import com.example.demo.service.ICache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: heal
 * @Date: 2023/7/5 16:15
 */
@Service
@Slf4j
public class AnswerServiceImpl {
    @Resource
    private ICache cacheService;

    @Resource
    private SessionRecordsMapper sessionRecordsMapper;
    @Resource
    private AccWorkTotalStatusMapper workTotalStatusMapper;

    private static final int SLEEP = 10000;

    @Async("interaction-thread")
    public void answer(String sessionId, Long accountId) {
        // 添加进失效Session
        cacheService.addInvalidSession(sessionId);
        // 接听电话
        log.info("方欣接口接听电话中------SessionID: {},AccountID: {}", sessionId, accountId);
        // 模拟通话结束后通知写入通话数据
        try {
            Thread.sleep(SLEEP);
            AnswerDTO answerDTO = sessionRecordsMapper.queryAnswerDTOByAccountId(accountId);
            // 返回通话结果
            SessionRecords sessionRecords = new SessionRecords()
                    .setSessionId(sessionId)
                    .setCaller("123456")
                    .setCalled("654321")
                    .setAccount(accountId)
                    .setOcId(answerDTO.getOcId())
                    .setStatus(SessionStatus.ANSWERED)
                    .setHandleState(SessionHandleState.COMPLETED)
                    .setCreateTime(LocalDateTime.now());
            saveSessionRecords(sessionRecords);
        } catch (InterruptedException e) {
            log.error("暂停线程报错：" + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存通话记录
     *
     * @param sessionRecords 通话记录
     */
    public void saveSessionRecords(SessionRecords sessionRecords) {
        sessionRecordsMapper.insert(sessionRecords);
        log.info("-------保存通话记录成功------");
        // 删除失效会话ID
        cacheService.removeInvalidSession(sessionRecords.getSessionId());
        // 添加空闲坐席，判断坐席是否已下线或忙碌中，未下线则添加进空闲坐席队列中
        AnswerDTO answerDTO = workTotalStatusMapper.getWorkStatusByAccount(sessionRecords.getAccount());
        log.info("-----坐席是否下线------");
        if (answerDTO == null) {
            log.info("坐席已下线---AccountId：{}", sessionRecords.getAccount());
            return;
        }
        cacheService.putIfAbsentUnusedAccount(answerDTO.getOcId(), answerDTO.getAccountId());
        log.info("坐席未下线---重新登记上线 OCID:{},AccountId:{}", answerDTO.getOcId(), answerDTO.getAccountId());
    }


}
