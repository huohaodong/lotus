package com.huohaodong.lotus.handler;

import com.huohaodong.lotus.filter.DefaultGatewayFilterChain;
import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.server.GatewayHttpClient;
import com.huohaodong.lotus.server.GatewayRequest;
import com.huohaodong.lotus.server.GatewayResponse;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class GatewayRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final GatewayRouter router = GatewayRouter.getInstance();

    private final AsyncHttpClient client = GatewayHttpClient.getInstance().client();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // 0. 根据客户端发来的 HTTP 请求构造 Context
        GatewayRequest gatewayRequest = new GatewayRequest(msg);
        GatewayResponse gatewayResponse = new GatewayResponse();
        GatewayContext gatewayContext = new GatewayContext(gatewayRequest, gatewayResponse, ctx);
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = remoteAddress.getAddress().getHostAddress();
        gatewayContext.attributes().put(GatewayContextAttributes.CLIENT_IP, clientIp);
        boolean isKeepAlive = HttpUtil.isKeepAlive(msg);
        gatewayContext.attributes().put(GatewayContextAttributes.KEEP_ALIVE, isKeepAlive);
        // 1. 根据客户端发来的 HTTP 请求匹配对应的 Route
        Optional<Route> route = router.route(gatewayContext);
        // 2. 根据匹配到的 Route 信息从缓存中获取或新构造 GatewayFilterChain
        route.ifPresentOrElse(
                r -> {
                    // 3. GatewayFilterChain.filter(GatewayContext)
                    new DefaultGatewayFilterChain(r.getFilters()).filter(gatewayContext);
                    // 4. 过滤后的请求转发给 Async Http Client 进行转发与响应
                    CompletableFuture<Response> future = client.executeRequest(gatewayContext.getRequest().builder().setUrl(String.valueOf(r.getUri())))
                            .toCompletableFuture();
                    future.whenComplete((response, throwable) -> {
                        if (response == null) {
                            if (isKeepAlive) {
                                ctx.writeAndFlush(NOT_FOUND_RESPONSE());
                            } else {
                                ctx.writeAndFlush(NOT_FOUND_RESPONSE()).addListener(ChannelFutureListener.CLOSE);
                            }
                        } else {
                            FullHttpResponse fullHttpResponse = gatewayContext.getResponse().builder()
                                    .headers(response.getHeaders())
                                    .httpVersion(HttpVersion.HTTP_1_1)
                                    .status(HttpResponseStatus.valueOf(response.getStatusCode()))
                                    .content(response.getResponseBody())
                                    .build();
                            if (isKeepAlive) {
                                ctx.writeAndFlush(fullHttpResponse);
                            } else {
                                ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                            }
                        }
                    });
                },
                () -> {
                    if (isKeepAlive) {
                        ctx.writeAndFlush(NOT_FOUND_RESPONSE());
                    } else {
                        ctx.writeAndFlush(NOT_FOUND_RESPONSE()).addListener(ChannelFutureListener.CLOSE);
                    }
                }
        );
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state().equals(IdleState.ALL_IDLE)) {
                ctx.channel().close();
            }
        }
        ctx.fireUserEventTriggered(evt);
    }

    public static FullHttpResponse NOT_FOUND_RESPONSE() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND,
                Unpooled.wrappedBuffer("404 Not Found".getBytes()));
    }
}
