package com.example.demo.controller;

import com.example.demo.utils.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author heal
 * @since 2023-07-01
 */
@Slf4j
@RestController
@RequestMapping("/session-records")
public class SessionRecordsController {

    @Resource
    private KafkaProducer<String> kafkaProducer;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping(value = "/kafka/sendMsg")
    public void sendMsg() {
//        kafkaProducer.send("OC1", "This is first message");
//        kafkaProducer.send("OC1", "This is second message");
//        kafkaProducer.send("OC1", "This is third message");
//        redisTemplate.opsForList().rightPush("OC2", "oc1");
//        redisTemplate.opsForList().rightPush("OC2", "oc2");
//        redisTemplate.opsForList().rightPush("OC2", "oc1");
//        redisTemplate.opsForList().rightPush("OC2", "oc3");
        log.info("redis 设置成功");
        Object oc1 = redisTemplate.opsForList().leftPop("OC2");
        Object oc2 = redisTemplate.opsForList().leftPop("OC2");
        Object oc3 = redisTemplate.opsForList().leftPop("OC2");
        Object oc4 = redisTemplate.opsForList().leftPop("OC2");
        Object oc5 = redisTemplate.opsForList().leftPop("OC2");
        System.out.println(oc1);
        System.out.println(oc2);
        System.out.println(oc3);
        System.out.println(oc4);
        System.out.println(oc5);
    }

    @KafkaListener(topics = "topic-interaction")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();

            log.info("Receive： +++++++++++++++ Topic:" + topic);
            log.info("Receive： +++++++++++++++ Record:" + record);
            log.info("Receive： +++++++++++++++ Message:" + message);
        }
    }
}
