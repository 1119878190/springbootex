package com.netty.netty.http;

import com.netty.netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            // 创建服务器端的启动对象，而皮质参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 使用链式变成来进行设置
            serverBootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)// 设置保持活动链接状态
                    .childHandler(new TestServerInitializer());

            System.out.println("===================服务器 is ready================");

            // 绑定一个端口并且同步，生成了一个ChannelFuture 对象
            // 启动服务器（并绑定端口）
            ChannelFuture cf = serverBootstrap.bind(7777).sync();

            // 给cf注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口7777成功");
                    } else {
                        System.out.println("监听端口失败");
                    }

                }
            });

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
