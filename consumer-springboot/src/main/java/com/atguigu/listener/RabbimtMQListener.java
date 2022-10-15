package com.atguigu.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbimtMQListener {
	@RabbitListener(queues = "boot_queue")
	public void listenerQueue(Message message) {
		System.out.println(new String(message.getBody()));
	}
}
