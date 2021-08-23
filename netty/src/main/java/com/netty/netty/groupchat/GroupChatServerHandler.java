package com.netty.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.*;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 点对点聊天使用一个hashmap管理
   // public static Map<String,Channel> channels = new HashMap<>();

    // 定义一个channel组，管理所有的channel
    // GlobalEventExecutor.INSTANCE 是全局的事件执行器是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // handlerAdded 表示连接建立，一旦建立连接，第一个被执行
    // 将当前channel 加入到 channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其它的在线客户端
        /**
         * 该方法会将 channelGroup 中所有的channel遍历，并发送消息，我们不需要自己遍历
         */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天" + sdf.format(new Date()) + " \n");
        channelGroup.add(channel);

        //channels.put("id100",channel);


    }

    /**
     * 断开连接,将xx客户离开信息推送给当前在线的客户
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
        System.out.println("channelGroup size：" + channelGroup.size());
    }

    /**
     * 表示channel处于活动的状态，提示xxx上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线~~~~~~");
    }

    /**
     * 表示channel处于不活动的状态，提示xxx离线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线~~~~~~");
    }

    /**
     * 读取数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        // 获取到当前channel
        Channel channel = ctx.channel();
        // 这时我们遍历chanelGroup，根据不同的情况，回送不同的消息

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                // 不是自己
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送消息" + msg + "\n");
            } else {
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }

    /**
     * 发送异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
