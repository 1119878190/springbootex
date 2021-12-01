package com.mp.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mp.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyUserMapper extends BaseMapper<User> {
}
