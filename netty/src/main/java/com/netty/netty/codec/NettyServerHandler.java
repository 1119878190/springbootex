package com.netty.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 1.自定义一个handler,需要继承Netty 规定好的某个HandleAdapter
 * 2.这时我们自定义一个Handler，才能称之为一个handler
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    // 读取数据的事件

    /**
     * 1 ChannelhandlerContext：上下文对象，含有管道pipeline，通道channel，地址
     * Object msg: 就是客户端发送的数据 默认时Object
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        // 读取从客户端发送的StudentPojo.Student
//        StudentPOJO.Student student = (StudentPOJO.Student) msg;
//        System.out.println("客户端发送的数据：id=" + student.getId() + "name=" + student.getName());
//    }

    /**
     * 数据读取完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 是write+flush
        // 将数据写入缓存，并刷新
        // 一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端,id:2  。", CharsetUtil.UTF_8));

    }

    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
            // 读取从客户端发送的StudentPojo.Student

        System.out.println("客户端发送的数据：id是=" + msg.getId() + "name是=" + msg.getName());
    }
}
