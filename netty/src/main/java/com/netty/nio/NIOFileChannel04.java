package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * 读取图片
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("d://a.jpg");
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("d://b.jpg");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        //使用transferForm 完成拷贝
        outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());

        //关闭相关通道
        outputStreamChannel.close();
        inputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();



    }
}
