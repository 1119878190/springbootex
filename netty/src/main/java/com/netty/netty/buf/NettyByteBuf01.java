package com.netty.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {

    public static void main(String[] args) {

        // 创建一个ByteBuf
        // 说明
        // 1.创建对象，该对象包含一个数组arr，是一个byte[10]
        // 2. 在netty 的buffer中，不需要使用flip进行反转
        //    底层维护了readerIndex 和 writeIndex
        // 3. 通过readerIndex 和 writeIndex  和 capacity，将buffer 分成了三个区域
        //   0------readerIndex  已经读取的区域
        //  readerIndex-------writeIndex  可读的区域
        //  writeIndex-------capacity   可写的区域
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        System.out.println("capacity="+byteBuf.capacity());

//        for (int i = 0; i < byteBuf.capacity(); i++) {
//            // 这个方法readerIndex不会发生改变
//            System.out.println(byteBuf.getByte(i));
//        }

        for (int i = 0; i < byteBuf.capacity(); i++) {
            // 这个方法readerIndex会发生改变
            System.out.println(byteBuf.readByte());
        }

        System.out.println("执行完毕");
    }
}
