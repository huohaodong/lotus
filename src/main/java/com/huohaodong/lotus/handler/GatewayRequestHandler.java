package com.huohaodong.lotus.handler;

import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.GatewayRequest;
import com.huohaodong.lotus.server.GatewayResponse;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class GatewayRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // 0. 根据客户端发来的 HTTP 请求构造 Context，或者直接使用 Netty 的 Channel Attribute，或者干脆不用 Context
        GatewayRequest gatewayRequest = new GatewayRequest(msg);
        GatewayResponse gatewayResponse = new GatewayResponse();
        GatewayContext gatewayContext = new GatewayContext(gatewayRequest, gatewayResponse, ctx);
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = remoteAddress.getAddress().getHostAddress();
        gatewayContext.attributes().put(GatewayContextAttributes.CLIENT_IP, clientIp);
        // 1. 根据客户端发来的 HTTP 请求匹配对应的 Route，根据 Route 构造 GatewayContext
        // TODO: 实现 Route 配置信息的读取已经 Route 信息中 Filter 和 Predicate 的构造，然后通过一个 handler 类来获取对应的映射信息
        // 2. 根据客户端发来的 HTTP 请求信息或根据 Route 中的信息从缓存中获取或新构造 GatewayFilterChain

        // 3. GatewayFilterChain.filter(GatewayContext)
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
