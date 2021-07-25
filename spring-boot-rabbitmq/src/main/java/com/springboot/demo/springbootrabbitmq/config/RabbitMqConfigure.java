package com.springboot.demo.springbootrabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfigure {


    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("test_directExchange01");
    }

    @Bean
    public Queue confirmQueue(){
        return new Queue("confirmQueue");
    }

    @Bean
    public Binding bingConfirm(){
        return BindingBuilder.bind(confirmQueue()).to(directExchange()).with("confirm");
    }
}
