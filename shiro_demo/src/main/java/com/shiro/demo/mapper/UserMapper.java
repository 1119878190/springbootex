package com.shiro.demo.mapper;

import com.shiro.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    public User queryUserByName(String name);
}
