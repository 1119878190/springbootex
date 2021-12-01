package com.mp.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mp.demo.pojo.User;

public interface MyUserService extends IService<User> {

    public void custom(User user);
}
