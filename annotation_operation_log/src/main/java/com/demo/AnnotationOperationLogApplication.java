package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AnnotationOperationLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnotationOperationLogApplication.class, args);
    }
}
