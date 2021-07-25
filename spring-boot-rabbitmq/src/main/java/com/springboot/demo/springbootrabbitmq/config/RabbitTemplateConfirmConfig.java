package com.springboot.demo.springbootrabbitmq.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class RabbitTemplateConfirmConfig implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);   //指定 ConfirmCallback
        rabbitTemplate.setReturnCallback(this);    //指定 ReturnCallback
        /**
         * true：
         * 交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：
         * 如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
    }

    /**
     * //消费方确认消息   到达exchange交换机
     * //通过实现 ConfirmCallback 接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，
     * // 也就是只确认是否正确到达 Exchange 中
     * @param correlationData
     * @param b
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("=============exchange确认====================");
        if (b){
            System.out.println("接受成功");
            System.out.println("消息唯一标识:"+correlationData);
            System.out.println("确认结果："+b);
        }else {
            System.out.println("接受失败：");
            System.out.println("消息唯一标识:"+correlationData);
            System.out.println("失败原因："+s);
        }
    }


    /**
     * //通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调
     * 启动消息失败返回，比如路由不到队列时触发回调
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("=============队列确认======================");
        System.out.println("消息主体 message : "+message);
        System.out.println("消息主体 message : "+replyCode);
        System.out.println("描述："+replyText);
        System.out.println("消息使用的交换器 exchange : "+exchange);
        System.out.println("消息使用的路由键 routing : "+routingKey);
    }
}
