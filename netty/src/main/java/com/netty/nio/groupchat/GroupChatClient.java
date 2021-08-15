package com.netty.nio.groupchat;

import javax.sound.sampled.Port;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    //定义相关属性
    private final String HOST = "127.0.0.1"; //服务器ip
    private final int PORT = 6667; //端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    //构造器

    public GroupChatClient() {

        try {
            selector = Selector.open();
            //连接服务器
            socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //将channel注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            //得到username
            username = socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(username + " is ok");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //向服务器发送消息
    public void sendInfo(String info){
        info = username + "说: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器段回复的消息
    public void readInfo(){
        try {
            int readChannel = selector.select();
            if (readChannel > 0){
                //有可用的通道

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()){
                        //得到相关通道
                         SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                         //得到一个buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        //读取
                        socketChannel.read(byteBuffer);
                        //把读道的缓冲区的数据转成字符串
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();//删除当前SelectionKey，防止重复操作
            }else {
//                System.out.println("没有可用的通道");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        //启动客户端
        GroupChatClient chatClient = new GroupChatClient();

        //启动一个线程
        new Thread(()->{
            while (true){
                chatClient.readInfo();
                try {
                    Thread.currentThread().sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
