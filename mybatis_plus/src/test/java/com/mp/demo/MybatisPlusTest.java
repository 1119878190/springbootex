package com.mp.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mp.demo.mapper.MyUserMapper;
import com.mp.demo.pojo.User;
import com.mp.demo.service.MyUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MybatisPlusTest {


    @Autowired
    private MyUserService userService;

    @Autowired
    private MyUserMapper myUserMapper;

    @Test
    public void list(){
        List<User> list = userService.list();
        list.forEach(item -> System.out.println(item));
    }

    @Test
    public void save(){
        List list = new ArrayList();
        list.add(new User("t1",12));
        list.add(new User("t2",13));
        list.add(new User("t3",14));
        list.add(new User("t4",15));

        userService.saveBatch(list);

        list.forEach(item -> System.out.println(item));
    }

    @Test
    public void get(){
        User user = userService.getById("7fbd013167750bda637da0bc612b3e76");
        System.out.println(user);
    }


    @Test
    public void builder(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,"3")
                .eq(User::getName,"liux");
        User one = userService.getOne(wrapper);
        System.out.println(one);
    }

    @Test
    public void update(){
        User user = new User();
        user.setId("3");
        user.setName("hahah");
        userService.custom(user);


    }

    @Test
    public void saveOne(){
        User user = new User();
        user.setName("389");
        user.setIdCard("234234");
        userService.save(user);
    }
}
