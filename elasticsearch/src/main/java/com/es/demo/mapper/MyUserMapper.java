package com.es.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.es.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyUserMapper extends BaseMapper<User> {
}
