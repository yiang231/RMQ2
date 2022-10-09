package com.atguigu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 确认模式：
	 * 步骤：
	 * 1. 确认模式开启：ConnectionFactory中开启publisher-confirms="true"
	 * 2. 在rabbitTemplate定义ConfirmCallBack回调函数
	 */
	@Test
	public void testConfirm() {
		//2. 定义回调
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack) {
					//接收成功
					System.out.println("接收成功消息" + cause);
				} else {
					//接收失败
					System.out.println("接收失败消息" + cause);
					//做一些处理,让消息再次发送。
				}
			}
		});
		//3. 发送消息
		//交换机和队列都正确
		//rabbitTemplate.convertAndSend("queueExchange", "queueConfirm", "交换机和队列都正确");
		//交换机正确，队列错误
		//rabbitTemplate.convertAndSend("queueExchange", "queueconfirm", "交换机正确，队列错误");
		//交换机和队列都错误
		rabbitTemplate.convertAndSend("queueexchange", "queueconfirm", "交换机和队列都错误");
	}

}
