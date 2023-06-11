package com.huohaodong.lotus.context;

import com.huohaodong.lotus.server.GatewayRequest;
import com.huohaodong.lotus.server.GatewayResponse;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GatewayContext {

    private final GatewayRequest request;

    private final GatewayResponse response;

    private final ChannelHandlerContext channelHandlerContext;

    private final Map<String, Object> attributes = new HashMap<>();

    public Map<String, Object> attributes() {
        return attributes;
    }

}
