package com.springboot.demo.springbootrabbitmq.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {

    private String name;

    private int age;

    private String remark;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
