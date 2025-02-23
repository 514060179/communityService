package com.newland.property.tcp.server;

import com.newland.property.core.log.LoggerFactory;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.util.StringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import org.slf4j.Logger;

/**
 * @author Moonny
 * &#064;Date  2024-05-28
 */
public class TCPServer {

    private final static Logger logger = LoggerFactory.getLogger(TCPServer.class);

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new TCPServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            String str = MappingCache.getValue("TCP_SERVER", "PORT");

            int port = StringUtil.isEmpty(str) ? 60000 : Integer.parseInt(MappingCache.getValue("TCP_SERVER", "PORT"));

            ChannelFuture future = bootstrap.bind(port).sync();
            logger.debug("TCP server started and listening on port " + port);

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}