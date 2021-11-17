package com.es.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.es.demo.mapper.MyUserMapper;
import com.es.demo.pojo.User;
import com.es.demo.service.MyUserService;
import org.springframework.stereotype.Service;

@Service
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper,User> implements MyUserService {

    @Override
    public void custom(User user){
        baseMapper.updateById(user);
    }

}
