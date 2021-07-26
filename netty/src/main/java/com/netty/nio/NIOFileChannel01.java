package com.netty.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//本地文件写入
public class NIOFileChannel01 {

    public static void main(String[] args) throws Exception {

        String str = "hello,nio";

        //创建输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d://file01.txt");

        //创建FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放入到buffer
        byteBuffer.put(str.getBytes());

        //对buffer进行反转
        byteBuffer.flip();

        //将buffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);

        //关闭输出流
        fileOutputStream.close();
    }
}
