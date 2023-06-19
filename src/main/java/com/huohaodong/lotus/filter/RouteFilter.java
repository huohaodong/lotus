package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.server.GatewayBootstrap;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletableFuture;

public class RouteFilter implements GatewayFilter {

    private RouteFilter() {
    }

    public static RouteFilter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static FullHttpResponse NOT_FOUND_RESPONSE() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND,
                Unpooled.wrappedBuffer("404 Not Found".getBytes()));
    }

    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {
        Route route = (Route) context.attributes().get(GatewayContextAttributes.ROUTE);
        boolean isKeepAlive = (Boolean) context.attributes().getOrDefault(GatewayContextAttributes.KEEP_ALIVE, false);
        // 过滤完成后的请求转发给 Async Http Client 进行转发与响应
        CompletableFuture<Response> future = GatewayBootstrap.asyncHttpClient.executeRequest(context.getRequest().builder().setUrl(String.valueOf(route.getUri())))
                .toCompletableFuture();
        future.whenComplete((response, throwable) -> {
            if (response == null) {
                if (isKeepAlive) {
                    context.getChannelHandlerContext().writeAndFlush(NOT_FOUND_RESPONSE());
                } else {
                    context.getChannelHandlerContext().writeAndFlush(NOT_FOUND_RESPONSE()).addListener(ChannelFutureListener.CLOSE);
                }
            } else {
                FullHttpResponse fullHttpResponse = context.getResponse().builder()
                        .headers(response.getHeaders())
                        .httpVersion(HttpVersion.HTTP_1_1)
                        .status(HttpResponseStatus.valueOf(response.getStatusCode()))
                        .content(response.getResponseBody())
                        .build();
                if (isKeepAlive) {
                    context.getChannelHandlerContext().writeAndFlush(fullHttpResponse);
                } else {
                    context.getChannelHandlerContext().writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                }
            }
        });
    }

    private static class SingletonHolder {
        private static final RouteFilter INSTANCE = new RouteFilter();
    }
}
