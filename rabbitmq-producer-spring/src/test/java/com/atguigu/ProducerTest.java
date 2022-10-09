package com.atguigu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
		//rabbitTemplate.convertAndSend("queueexchange", "queueconfirm", "交换机和队列都错误");
	}

	//回退模式
	@Test
	public void testReturn() {
		//Mandatory 代理 委托  只有配置该项，return模式才会返回消息
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText,
										  String exchange, String routingKey) -> {
			System.out.println("message = " + new String(message.getBody()));
			System.out.println("message.getMessageProperties() = " + message.getMessageProperties());
			System.out.println("replyCode = " + replyCode);
			System.out.println("replyText = " + replyText);
			System.out.println("exchange = " + exchange);
			System.out.println("routingKey = " + routingKey);
		});
		//成功发给Exchange和Queue:如果成功，return没有反馈
		for (int i = 0; i < 10000; i++) {
			rabbitTemplate.convertAndSend("queueExchange", "queueConfirm", "成功发给Exchange和Queue" + i);
		}

		//交换机正确，队列错误
		/*for (int i = 0; i < 10; i++) {
			rabbitTemplate.convertAndSend("queueExchange", "queueconfirm", "交换机正确，队列错误");
		}*/

		System.out.println("消息发送完毕");
	}

	@Test
	public void testttl() {
		MessagePostProcessor messagePostProcessor = (Message message) -> {
			message.getMessageProperties().setExpiration("50000");
			return message;
		};
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//成功发给Exchange和Queue:如果成功，return没有反馈
		for (int i = 0; i < 10; i++) {
			rabbitTemplate.convertAndSend("topicExchange", "ttl.a", "ttl成功发给Exchange和Queue" + i, messagePostProcessor);
		}
		System.out.println("消息发送完毕");
	}

	/*死信队列过期时间*/
	@Test
	public void testdlx1() {
		rabbitTemplate.convertAndSend("exchangedlx", "dlx.ttl", "测试消息过期进入死信队列");
	}

	/*死信队列超出长度*/
	@Test
	public void testdlx2() {
		for (int i = 0; i < 100; i++) {
			this.rabbitTemplate.convertAndSend("exchangedlx", "dlx.maxlength", "测试消息超过队列存储消息的长度进入死信队列" + i);
		}
		try {
			Thread.sleep(7000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.rabbitTemplate.convertAndSend("exchangedlx", "dlx.maxlength", "测试消息超过队列存储消息的长度进入死信队列");

	}

	/*死信队列消息拒收*/
	@Test
	public void testdlx3() {
		rabbitTemplate.convertAndSend("exchangedlx", "dlx.refuse", "测试手动确认拒收进入死信队列");
	}
}