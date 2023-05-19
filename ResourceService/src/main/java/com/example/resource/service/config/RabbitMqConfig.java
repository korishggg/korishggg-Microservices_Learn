package com.example.resource.service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Bean
	public Queue createResourceQueue(){
		return new Queue("create-resource");
	}

	@Bean
	public FanoutExchange createResourceExchange(){
		return new FanoutExchange("create-resource-exchange");
	}

	@Bean
	public Binding createResourceBinding(Queue createResourceQueue,
										 FanoutExchange fanoutExchange){
		return BindingBuilder.bind(createResourceQueue).to(fanoutExchange);
	}

}
