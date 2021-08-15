package com.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 1.自定义一个handler,需要继承Netty 规定好的某个HandleAdapter
 * 2.这时我们自定义一个Handler，才能称之为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据的事件
    /**
     * 1 ChannelhandlerContext：上下文对象，含有管道pipeline，通道channel，地址
     * Object msg: 就是客户端发送的数据 默认时Object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 比如这里我们有一个非常耗时长的业务 -> 异步执行 -> 提交该channel 对应的NIOEventLoop 的taskQueue中

        // 解决方案1 ： 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端，id:1  。",CharsetUtil.UTF_8));
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端，id:3  。",CharsetUtil.UTF_8));
            }
        });

        // 2. 用户自定义定时任务  --->  该任务时提交到 scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端，id:4  。",CharsetUtil.UTF_8));
            }
        },5, TimeUnit.SECONDS);


        System.out.println("go on......");

//        System.out.println("服务器读取线程"+Thread.currentThread().getName());
//        System.out.println("server ctx : "+ctx);
//        System.out.println("看看channel和pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链表，出战入站
//        // 将msg转成一个ByteBuffer
//        // ByteBuf 时netty提供的，不是NIO的Byteffer
//        ByteBuf byteBuffer = (ByteBuf)msg;
//        System.out.println("客户端发送消息是："+ byteBuffer.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ channel.remoteAddress());

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 是write+flush
        // 将数据写入缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
       ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端,id:2  。",CharsetUtil.UTF_8));

    }

    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
