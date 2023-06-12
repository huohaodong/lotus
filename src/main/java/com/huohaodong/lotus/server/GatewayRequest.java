package com.huohaodong.lotus.server;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.RequestBuilder;

import java.util.Set;

@RequiredArgsConstructor
public class GatewayRequest {
    /**
     * 原始 HTTP 请求报文
     */
    @Getter
    private final FullHttpRequest fullHttpRequest;

    /**
     * 交由 AsyncHttpClient 发送给下游服务的请求
     */
    @Getter
    private final RequestBuilder requestBuilder = new RequestBuilder();
    private Set<Cookie> cookies;

    public RequestBuilder builder() {
        return requestBuilder;
    }

    public Set<Cookie> cookies() {
        if (cookies == null) {
            cookies = ServerCookieDecoder.STRICT.decode(headers().get(HttpHeaderNames.COOKIE));
        }
        return cookies;
    }

    public HttpHeaders headers() {
        return fullHttpRequest.headers();
    }
}
