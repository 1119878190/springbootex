package com.shiro.demo;


import com.shiro.demo.pojo.User;
import com.shiro.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShiroSpringbootApplicationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void test(){
        User user = userService.queryUserByName("zhangsan");
        System.out.println("姓名："+user.getName()+"密码："+user.getPassword());
    }
}
