package com.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 入站的handler 进行解码MyByteToLOngDecoder
        pipeline.addLast(new MyByteToLOngDecoder());

        // 自定义handler处理逻辑
        pipeline.addLast(new MyServerHandler());
    }
}
