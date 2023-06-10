package com.huohaodong.lotus.request;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

@Data
public class GatewayRequest {

    /**
     * 原始 HTTP 请求
     */
    private final FullHttpRequest fullHttpRequest;

    /**
     * HTTP 请求的全局 id
     */
    private final String requestId;

    /**
     * HTTP 请求的用户 ip
     */
    private final String clientIp;

}
