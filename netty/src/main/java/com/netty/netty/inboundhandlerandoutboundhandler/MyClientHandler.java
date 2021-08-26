package com.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器id=" + ctx.channel().remoteAddress());
        System.out.println("收到服务器消息=" + msg);
    }

    // 重写channelActive 发送数据

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);//发送的是long

        // 1.这里的字符串是16个字节
        // 2.该处理器的前一个handler 是 MyLongToByteEncoder
        // 3.如果写入的数据类型不是encoder中的数据类型，跳过编码，直接发送
        //ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
