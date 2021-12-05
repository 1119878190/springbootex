package com.demo.controller;


import com.demo.annotions.Log;
import com.demo.entity.ResponseVo;
import com.demo.entity.UserEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @PostMapping("/testLog")
    @Log(operDesc = "testLog被调用")
    public ResponseVo testLog(@RequestBody UserEntity user) {
        System.out.println("controller被调用，前端传的参数为：" + user.toString());

        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);
        responseVo.setMsg("controller成功调用");
        return responseVo;
    }


    @PostMapping("/testLog2")
    @Log()
    public ResponseVo testLog2(@RequestParam("userName") String userName, @RequestParam("age") int age) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);
        responseVo.setMsg("controller成功调用");
        return responseVo;
    }
}
