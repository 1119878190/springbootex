package com.netty.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLOngDecoder extends ByteToMessageDecoder {

    /**
     * decode 方法 会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list
     * 或者是ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空，就会将list的内容传递给下一个 channelInboundHandler 处理，该处理器的方法也会被调用多次
     * @param ctx 上下文对象
     * @param in    入站的bytebuf
     * @param out   List 集合  ，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // 因为long8个字节,需要判断有8个字节，才能读取一个
        if (in.readableBytes() >=8){
            out.add(in.readLong());
        }
    }
}
