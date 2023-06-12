package com.huohaodong.lotus.server;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;
import org.asynchttpclient.RequestBuilder;

@Data
public class GatewayRequest {
    /**
     * 原始 HTTP 请求报文
     */
    private final FullHttpRequest fullHttpRequest;

    /**
     * 交由 AsyncHttpClient 发送给下游服务的请求
     */
    private final RequestBuilder requestBuilder = new RequestBuilder();

    public RequestBuilder builder() {
        return requestBuilder;
    }
}
