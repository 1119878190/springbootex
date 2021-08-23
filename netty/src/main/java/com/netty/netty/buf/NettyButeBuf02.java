package com.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyButeBuf02 {
    public static void main(String[] args) {

        // 创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));

        // 使用相关的方法
        if (byteBuf.hasArray()){ // true
            byte[] content = byteBuf.array();

            System.out.println(new String(content,Charset.forName("utf-8")));

            System.out.println("byteBuf="+byteBuf);

            System.out.println(byteBuf.arrayOffset()); //0
            System.out.println(byteBuf.readerIndex()); //0
            System.out.println(byteBuf.writerIndex()); //12
            System.out.println(byteBuf.capacity()); // 64

            System.out.println(byteBuf.readByte()); //读数据，可读的字节数会变

            int len = byteBuf.readableBytes(); // 可读的字节数 12
            System.out.println("len="+len);

            // 范围读取
            System.out.println(byteBuf.getCharSequence(0,4, CharsetUtil.UTF_8));
            String a = "234234234";
        }
    }
}
