package com.springboot.demo.springbootrabbitmq.controller;

import com.springboot.demo.springbootrabbitmq.entity.UserEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class UserController {


    @Value("${test.name}")
    private String name;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @GetMapping("/test")
    public void test(){
        System.out.println(name);
    }

    @GetMapping("/confirm")
    public void testRabbitmq(){
        UserEntity user = new UserEntity();
        user.setAge(23);
        user.setName("zhangsan");
        user.setRemark("这个是rabbitmq消息确认测试用户");
        CorrelationData correlationData = new CorrelationData("user:zhangsan");
        rabbitTemplate.convertAndSend("test_directExchange01","confirm",user,correlationData);

        UserEntity userError = new UserEntity();
        user.setAge(50);
        user.setName("lisi");
        user.setRemark("测试routingkey错误");
        CorrelationData correlationData2 = new CorrelationData("user:lisi");
        rabbitTemplate.convertAndSend("test_directExchange01","error_routingkey",userError,correlationData2);
    }

}
