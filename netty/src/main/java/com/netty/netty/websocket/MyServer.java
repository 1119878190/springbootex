package com.netty.netty.websocket;

import com.netty.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();// 默认Cup核数的2倍 8个NIOEventLoop

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//在bossgroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 因为基于http协议，使用http的编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 是以块方式写，添加 ChunkedWrite 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * 说明：
                             * 1.http数据在传输过程中是分段的，HttpObjectAggregator,就是可以将多个段聚合起来
                             * 2.这就是为什么，当浏览器发送大量数据时，就会发送多次HTTP请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 说明：
                             * 1.对于websocket，它的数据都是以 帧（frame） 新式传递的
                             * 2.额可以看到WebSocketFrame 下面有六个子类
                             * 3.浏览器请求时 ws://localhost:7000/hello  表示请求的uri
                             * 4.WebSocketServerProtocolHandler 核心功能时将http协议升级为ws协议，保持长连接
                             * 5.是通过一个状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义handler,处理业务逻辑
                            pipeline.addLast(new MyTestWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
