package com.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class MyClientInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 加入一个出站的handler，对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());

        // 解码器
        //pipeline.addLast(new MyByteToLOngDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());

        // 加入一个自定义handler 处理逻辑
        pipeline.addLast(new MyClientHandler());
    }
}
