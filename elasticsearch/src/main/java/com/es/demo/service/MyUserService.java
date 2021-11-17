package com.es.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.es.demo.pojo.User;

public interface MyUserService extends IService<User> {

    public void custom(User user);
}
