package com.exceptionresolve.demo.controller;

import com.exceptionresolve.demo.entity.ResponseVo;
import com.exceptionresolve.demo.exception.MyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常测试
 */
@RestController
public class ExceptionController {

    @GetMapping("/testException")
    public ResponseVo testMyException() {
        ResponseVo responseVo = new ResponseVo();
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
        return responseVo;
    }

}
