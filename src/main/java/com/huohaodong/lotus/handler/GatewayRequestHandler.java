package com.huohaodong.lotus.handler;

import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.GatewayRequest;
import com.huohaodong.lotus.server.GatewayResponse;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Optional;

@Slf4j
public class GatewayRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final GatewayRouter router = GatewayRouter.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // 0. 根据客户端发来的 HTTP 请求构造 Context
        GatewayRequest gatewayRequest = new GatewayRequest(msg);
        GatewayResponse gatewayResponse = new GatewayResponse();
        GatewayContext gatewayContext = new GatewayContext(gatewayRequest, gatewayResponse, ctx);
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = remoteAddress.getAddress().getHostAddress();
        gatewayContext.attributes().put(GatewayContextAttributes.CLIENT_IP, clientIp);
        // 1. 根据客户端发来的 HTTP 请求匹配对应的 Route
        Optional<Route> route = router.route(gatewayContext);
        // 2. 根据匹配到的 Route 信息从缓存中获取或新构造 GatewayFilterChain
        // 3. GatewayFilterChain.filter(GatewayContext)
        // 4. 过滤后的请求转发给 Async Http Client 进行转发与响应
        System.out.println(route);
        // TODO: 目前不做转发，默认返回 OK，方便调试，完成转发逻辑后移除
        ctx.writeAndFlush(new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer("lotus gateway".getBytes()))).addListener(ChannelFutureListener.CLOSE);
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
}
