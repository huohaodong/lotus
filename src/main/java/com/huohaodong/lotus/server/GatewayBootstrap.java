package com.huohaodong.lotus.server;

import com.huohaodong.lotus.handler.GatewayRequestHandler;
import com.huohaodong.lotus.route.RouteDefinition;
import com.huohaodong.lotus.server.properties.GatewayProperties;
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
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.yaml.snakeyaml.Yaml;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class GatewayBootstrap {

    private GatewayProperties gatewayProperties;

    private EventLoopGroup bossEventLoopGroup;

    private EventLoopGroup workerEventLoopGroup;

    private AsyncHttpClient asyncHttpClient;

    private void loadGatewayProperties() {
        // YAML -> Object
        gatewayProperties = new Yaml().loadAs(this.getClass().getClassLoader().getResourceAsStream("GatewayProperties.yml"), GatewayProperties.class);
        // Object -> Route Definition (Filter Definition + Predicate Definition + uri + order + id)
        List<RouteDefinition> routeDefinitions = gatewayProperties.populate();
        log.info("load Route Definitions: {}, from GatewayProperties.yml: ", routeDefinitions);
        // TODO: Route Definition -> Route

    }

    private void startGatewayServer() {
        bossEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        workerEventLoopGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
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
            log.error("Error when init gateway", e);
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
            log.error("Error when shutdown gateway", e);
        }
    }
}
