package com.netty.bio;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class BIOServer {

    public static void main(String[] args) throws IOException {

        //创建线程池
      ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,
              8,
              30,
              TimeUnit.SECONDS,
              new ArrayBlockingQueue<>(5),
              Executors.defaultThreadFactory(),
              new ThreadPoolExecutor.AbortPolicy());

      //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true){
            System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
            //监听，等待客户端链接
            System.out.println("等待连接....");
            //会阻塞在accept()
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程与之通信
            threadPoolExecutor.execute(()->{
                handler(socket);
            });
        }
    }

    //编写一个handler方法，和客户端通讯
    public static void handler(Socket socket){

        byte[] bytes = new byte[1024];
        try {
            System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            int size;

            while ( (size = inputStream.read(bytes)) != -1){
                System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
                System.out.println("read....");//d读不到数据会阻塞
                    System.out.println(new String(bytes,0,size));//输出客户端发送的数据
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
