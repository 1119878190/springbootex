package com.redission.demo;

import com.redission.demo.controller.TestController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.redisson.api.RedissonClient;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDemo {


    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redission(){
        System.out.println(redissonClient);
    }
}
