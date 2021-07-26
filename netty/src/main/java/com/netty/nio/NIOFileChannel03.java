package com.netty.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//文件拷贝
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {
        File file = new File("d://file01.txt");

        FileInputStream fileInputStream = new FileInputStream(file);
        //获取channel
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        //写出
        FileOutputStream fileOutputStream = new FileOutputStream("d://file02.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();



        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(521);

        while (true){

            //这里有一个重要的操作，一定不要忘了
            /*
            public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }
            */
            byteBuffer.clear();//清空buffer

            //从channel读取数据到buffer
            int read = inputStreamChannel.read(byteBuffer);
            if (read == -1){
                break;
            }
            System.out.println(new String(byteBuffer.array()));

            //从原来的buffer中读取数据到channel中
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }

    }
}
