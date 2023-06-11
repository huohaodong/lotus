package com.huohaodong.lotus.server;

import com.huohaodong.lotus.handler.GatewayRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.yaml.snakeyaml.Yaml;

import java.net.InetSocketAddress;

public class GatewayBootstrap {

    private GatewayProperties gatewayProperties;

    private EventLoopGroup bossEventLoopGroup;

    private EventLoopGroup workerEventLoopGroup;

    private ServerBootstrap serverBootstrap;

    private AsyncHttpClient asyncHttpClient;

    private void loadGatewayProperties() {
        gatewayProperties = new Yaml().loadAs(this.getClass().getClassLoader().getResourceAsStream("GatewayProperties.yml"), GatewayProperties.class);
    }

    private void startGatewayServer() {
        bossEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        workerEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap()
                .group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(),
                                new HttpObjectAggregator(gatewayProperties.getMaxContentLength()),
                                new IdleStateHandler(0, 0, gatewayProperties.getHeartBeatTimeoutSecond()),
                                new GatewayRequestHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(gatewayProperties.getHost(), gatewayProperties.getPort())).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGatewayHttpClient() {
        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setEventLoopGroup(workerEventLoopGroup)
                .setConnectTimeout(gatewayProperties.getConnectTimeout())
                .setRequestTimeout(gatewayProperties.getRequestTimeout())
                .setMaxRedirects(gatewayProperties.getMaxRedirects())
                .setMaxRequestRetry(gatewayProperties.getMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT)
                .setCompressionEnforced(true)
                .setMaxConnections(gatewayProperties.getMaxConnections())
                .setMaxConnectionsPerHost(gatewayProperties.getMaxConnectionsPerHost())
                .setPooledConnectionIdleTimeout(gatewayProperties.getPooledConnectionIdleTimeout());
        this.asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
    }

    public void start() {
        loadGatewayProperties();
        startGatewayServer();
        startGatewayHttpClient();
    }

    public void shutdown() {
        try {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
            asyncHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
