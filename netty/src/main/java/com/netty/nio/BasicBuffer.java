package com.netty.nio;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {

        //创建一个 Buffer，大小为 5，即可以存放 5 个 int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            //写入值
            intBuffer.put(i * 2);
        }

        //Buffer切换读写
        intBuffer.flip();
        //当Buffer中还有数据时
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
