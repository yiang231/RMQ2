package com.atguigu;

import com.atguigu.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 第一个参数：交换机名字
	 * 第二个参数：routingKey
	 * 第三个参数：发送的消息
	 */
	@Test
	public void testSend() {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "boot.haha", "mq hello");
	}
}
