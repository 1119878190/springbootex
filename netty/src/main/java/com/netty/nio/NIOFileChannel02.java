package com.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//文件读取
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {

        //创建输入流
        File file = new File("d://file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //获取Channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读入到buffer中
        inputStreamChannel.read(byteBuffer);

        //将字节数据转换成String
        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
    }
}
