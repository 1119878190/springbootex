package com.mp.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.demo.mapper.MyUserMapper;
import com.mp.demo.pojo.User;
import com.mp.demo.service.MyUserService;
import org.springframework.stereotype.Service;

@Service
public class MyUserServiceImpl extends ServiceImpl<MyUserMapper, User> implements MyUserService {

    @Override
    public void custom(User user){
        baseMapper.updateById(user);
    }

}
