package com.atguigu.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class ManualAckListener implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		try {

			System.out.println(deliveryTag);
			System.out.println("message:" + new String(message.getBody()));
			System.out.println("-------------------------");
			//写上这个语句，100%出异常
			//实际中可能是因为网络问题，第一次没有成功，后面网络拥堵断开问题解决，重复读取消息就可能成功
			//也可以在这里指定一个1以内的随机数，0的时候失败，1时候成功
			int i = 10 / 0;
			/**
			 * @param deliveryTag 消息的编号
			 * @param multiple
			 * true to acknowledge all messages ！！up to！！ and including the supplied delivery tag;
			 *    确认=deliveryTag消息，同时还确认了所有deliveryTag的消息
			 * false to acknowledge just the supplied delivery tag.
			 *   只确认当前deliveryTag的消息
			 */
			//手动确认，消息成功
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			e.printStackTrace();
			//手动确认，消息失败
			// requeue
			// true ：重新放回队列，那不就可能死循环了吗
			//false ：放入死信队列。没有死信队列，舍弃
			//channel.basicNack(deliveryTag,false,true);
			channel.basicNack(deliveryTag, false, false);
		}
	}
}
