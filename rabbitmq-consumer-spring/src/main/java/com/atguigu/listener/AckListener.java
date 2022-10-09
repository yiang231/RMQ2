package com.atguigu.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class AckListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		//默认情况下队列中消息会一次性的取出来
		//每执行一次回调函数，处理一个消息
		//默认自动确认
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		System.out.println(deliveryTag);
		System.out.println("message:" + new String(message.getBody()));
		System.out.println("-------------------------");

		//int i = 10 / 0;
	}
}
