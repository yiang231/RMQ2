package com.atguigu.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String EXCHANGE_NAME = "boot_topic_exchange";
	public static final String QUEUE_NAME1 = "boot_queue1";
	public static final String QUEUE_NAME2 = "boot_queue2";

	// 1 交换机
	@Bean("bootExchange")
	public Exchange bootExchange() {
		return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
	}

	//2.Queue 队列
	@Bean("bootQueue1")
	public Queue bootQueue1() {
		System.out.println("队列一创建完毕");
		return QueueBuilder.durable(QUEUE_NAME1).build();
	}

	@Bean("bootQueue2")
	public Queue bootQueue2() {
		System.out.println("队列二创建完毕");
		return QueueBuilder.durable(QUEUE_NAME2).build();
	}

	//3. 队列和交互机绑定关系 Binding
    /*
        1. 知道哪个队列
        2. 知道哪个交换机
        3. routing key
        noargs()：表示不指定参数
     */
	@Bean
	public Binding bindQueueExchange1(@Qualifier("bootQueue1") Queue queue111,
									  @Qualifier("bootExchange") Exchange exchange) {
		System.out.println("绑定完毕");
		return BindingBuilder.bind(queue111).to(exchange).with("*.rabbit.*").noargs();
	}

	@Bean
	public Binding bindQueueExchange2(@Qualifier("bootQueue2") Queue queue222,
									  @Qualifier("bootExchange") Exchange exchange) {
		System.out.println("绑定完毕");
		return BindingBuilder.bind(queue222).to(exchange).with("lazy.#").noargs();
	}
}
