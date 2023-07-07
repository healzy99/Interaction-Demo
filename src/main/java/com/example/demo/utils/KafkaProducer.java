package com.example.demo.utils;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * @Author: heal
 * @Date: 2023/7/3 14:48
 */
@Component
@Slf4j
public class KafkaProducer<T> {
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;
    //自定义topic
    public static final String TOPIC = "topic-interaction";

    public void send(String key, T obj) {
        log.info("kafka send message = {}", obj);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, key, obj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("Produce: The message failed to be sent: {}", throwable.getMessage());
                log.error("Error: " + throwable);
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                log.info("Produce: The message was sent successfully: {}", stringObjectSendResult.toString());
            }
        });
    }
}
