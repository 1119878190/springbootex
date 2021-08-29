package com.redission.demo.controller;


import io.micrometer.core.instrument.util.TimeUtils;
import jodd.time.TimeUtil;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/redission")
public class TestController {


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redission  简单使用
     *重点：看门狗
     * @return
     */
    @GetMapping("/test")
    public String hello() {

        // 1.获取一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");

        // 2.加锁  阻塞时等待，默认加的锁都是30s时间
        //lock.lock();
        lock.lock(10, TimeUnit.SECONDS);// 自定义10秒自动解锁，自定解锁时间一定要大于业务执行的时间
        // 问题：lock.lock(10, TimeUnit.SECONDS)，在锁时间到了以后，如果业务还没有执行完毕，不会自动续期
        // 1.如果我们传递了锁的超时时间，就发送redis执行脚本，进行战锁，默认超时时间就是我们指定的时间
        // 2.如果我们未指定锁的超时时间，就是用 30 * 1000 【LockWatchdogTimeout看门狗默认时间30s】
        // 3.只要战锁成功就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗默认时间】，每隔10s自动再次续期到30s
        // 4.定时任务每 10s(LockWatchdogTimeout/3)执行续期

        // 最佳实战  使用lock.lock(10, TimeUnit.SECONDS)，省掉了整个续期操作
        try {
            System.out.println("加锁成功，执行业务" + Thread.currentThread().getId());
            // 1.锁的自动续期，如果业务超长，运行期间会自动给锁续上新的30s，不用担心业务时间长，锁自动过期被删掉【看门狗机制】
            // 2.加锁的业务只要完成，就不会给当前锁续期，即使不动手解锁，锁默认在30s以后自动删除

            Thread.sleep(3000);// 处理业务
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 解锁  假设解锁代码没有运行，redission会
            System.out.println("解锁成功" + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";

    }


    /**
     * 读写锁  写锁：排他锁   读锁：共享锁
     * 读写锁：保证一定能读到最新数据，修改期间，写锁是一个排他锁（互斥锁），写锁没释放，读就必须等待
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() throws InterruptedException {

        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("my-lock");
        RLock rLock = readWriteLock.writeLock();

        String s = "";
        try {
            // 1.该数据加写锁，读数据加读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    @GetMapping("/read")
    public String readValue() throws InterruptedException {

        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("my-lock");
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        String s = "";
        try {
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }


        return s;
    }


    /**
     * countDownLanuth
     * 5个全部执行完毕才结束
     *
     * @return
     */
    @GetMapping("/lockDoor")
    public void lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);//指定完成个数5个
        door.await();// 等待闭锁全部完成

        System.out.println("全部执行完毕");
    }

    public void gogogo() {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();// 计数减一，每执行一次，就会减1，5次全部减完，会执行await方法结束
    }


    /**
     * semaphore
     *可以用作限流，最大并发量
     * @throws InterruptedException
     */
    public void park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
       // park.acquire();//获取一个信号量，获取一个值,如果没有信号量，则阻塞式等待
        boolean b = park.tryAcquire();// 非阻塞式等待
        if (b){

        }else {
            System.out.println("当前稍后再试");
        }


    }

    public void go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();//释放一个信号

    }

}
