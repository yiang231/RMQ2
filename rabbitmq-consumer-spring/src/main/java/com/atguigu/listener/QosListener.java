package com.atguigu.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class QosListener implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		try {
			System.out.println(deliveryTag);
			System.out.println("message:" + new String(message.getBody()));
			System.out.println("-------------------------");
			//int i=10/0;
			//手动确认消息发送成功
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			e.printStackTrace();
			channel.basicNack(deliveryTag, false, false);
		}
	}
}
