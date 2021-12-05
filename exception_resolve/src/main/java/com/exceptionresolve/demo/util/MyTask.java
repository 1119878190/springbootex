package com.exceptionresolve.demo.util;

import java.util.concurrent.Callable;

public class MyTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getId() + "开始执行任务成功");
        //int i = new Random().nextInt(10) * 1000;
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getId() + "*******任务执行完毕*******");
        return Thread.currentThread().getId()+"================完成了工作=================";
    }
}
