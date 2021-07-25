package com.springboot.demo.springbootrabbitmq.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class RabbitTemplateReturnConfig implements RabbitTemplate.ReturnCallback {

    //@Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback
    }

   
}
