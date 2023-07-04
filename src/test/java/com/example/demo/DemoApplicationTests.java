package com.example.demo;

import com.example.demo.utils.KafkaProducer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class DemoApplicationTests {

	@Resource
	private AdminClient adminClient;

	@Autowired
	private KafkaProducer<String> kafkaProducer;

	@Test
	void contextLoads() {
		// 测试动态创建topic
		NewTopic testTopic = new NewTopic("topic-interaction", 2, (short) 1);
		adminClient.createTopics(Collections.singletonList(testTopic));
	}

	@Test
	void getAllTopic() throws ExecutionException, InterruptedException {
		// 测试查询全部topic
		ListTopicsResult result = adminClient.listTopics();
		Set<String> strings = result.names().get();
		strings.forEach(System.out::println);
	}

	@Test
	void sendMessage(){
		kafkaProducer.send("OC1","This is first message");
	}
}
