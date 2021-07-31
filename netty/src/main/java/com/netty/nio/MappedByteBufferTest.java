package com.netty.nio;

import com.sun.media.jfxmediaimpl.HostUtils;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/**
 * 说明 1.MappedByteBuffer 可让文件直接在内存（堆外内存）修改,操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("d://file01.txt", "rw");
        //获取对应的channel
        FileChannel channel = randomAccessFile.getChannel();

        //获取BUffer
        /**
         *  参数 1:FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数 2：  0：可以直接修改的起始位置
         * 参数 3:    5: 是映射到内存的大小（不是索引位置），即将 1.txt 的多少个字节映射到内存
         *可以直接修改的范围就是 0-5  5个字节   下表0-4不包括5
         * 实际类型 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) 'H');
        mappedByteBuffer.put(3,(byte) '9');
        //mappedByteBuffer.put(5,(byte) 'y');//IndexOUtOfBoundException

        randomAccessFile.close();
        System.out.println("修改成功");

    }
}
