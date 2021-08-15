package com.netty.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 聊天室  服务端
 * 1.服务器启动监听6667端口
 * 2.服务器接受客户端信息，并实现转发【处理上线和离线】
 * 3
 */
public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    public GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将listenChannel注册到Selector上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            //循环处理
            while (true) {

                int count = selector.select();
                if (count > 0) { //有事件处理

                    //遍历得到的selectionKey
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        //取出selectionkey
                        SelectionKey selectionKey = iterator.next();

                        //如果监听到了accept
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            //将该socketChannel注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }

                        if (selectionKey.isReadable()) {//通道发生read状态，即通道时可读状态
                            //处理读
                            readData(selectionKey);
                        }

                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待。。。。。。。");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //读取客户端消息
    private void readData(SelectionKey key) {
        //定义一个SocketChannel
        SocketChannel channel = null;

        try {
            //取到关联的channel
            channel = (SocketChannel) key.channel();
            //创建缓冲
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

            //根据count的值做处理
            if (count > 0) {
                //有数据，把缓冲区的数据转成字符串
                String msg = new java.lang.String(buffer.array());
                //输出该消息
                System.out.println("from 客户端：" + msg);

                //向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }

        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了。。。。。");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * 转发消息给其他客户端（通道）
     *
     * @param msg  消息
     * @param self 自己的通道，要排除自己，不给自己发消息
     */
    public void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息：");
        //遍历 所有注册到selector上的SocketChannel，并排除自己
        for (SelectionKey key : selector.keys()) {

            //通过key 去除对应的SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {

                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {

       //创建服务器对象
        GroupChatServer chatServer = new GroupChatServer();
        chatServer.listen();
    }



}
