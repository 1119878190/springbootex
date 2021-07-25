package com.springboot.demo.springbootrabbitmq.service;


import com.rabbitmq.client.Channel;
import com.springboot.demo.springbootrabbitmq.entity.UserEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = {"confirmQueue"})
public class ConsumerService {


    /**
     * 消费方消息确认
     * @param user
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void consumerConfirm(UserEntity user, Message message , Channel channel) throws IOException {
        try {
            System.out.println("===============消费者确认===================");
            System.out.println("消费者消费消息：" + user.getName());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            System.out.println("消费消息确认" + message.getMessageProperties().getConsumerQueue() + "，接收到了回调方法");
        } catch (Exception e) {
            //重新回到队列
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//            System.out.println("尝试重发：" + message.getMessageProperties().getConsumerQueue());
            //requeue =true 重回队列，false 丢弃
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            // TODO 该消息已经导致异常，重发无意义，自己实现补偿机制


        }

    }
}
