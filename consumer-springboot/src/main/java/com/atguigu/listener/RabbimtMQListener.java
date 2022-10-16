package com.atguigu.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/*类名任意，建议以Listener结尾，不需要实现指定的接口或者继承类
 * 方法名任意，必须带参数message*/
@Component
public class RabbimtMQListener {
	@RabbitListener(queues = "boot_queue1")
//	@RabbitListener(queues = {"boot_queue1","boot_queue2"})
	public void listenerQueue1(Message message) {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		System.out.println("deliveryTag = " + deliveryTag);
		System.out.println(new String(message.getBody()));
		System.out.println("=====================");
	}

	@RabbitListener(queues = "boot_queue2")
	public void listenerQueue2(Message message) {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		System.out.println("deliveryTag = " + deliveryTag);
		System.out.println(new String(message.getBody()));
		System.out.println("=====================");
	}
}
