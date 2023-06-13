package com.huohaodong.lotus.server;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import org.asynchttpclient.RequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GatewayRequest {
    /**
     * 原始 HTTP 请求报文
     */
    private final FullHttpRequest fullHttpRequest;

    /**
     * 交由 AsyncHttpClient 发送给下游服务的请求
     */
    private final RequestBuilder requestBuilder;

    private final QueryStringDecoder queryStringDecoder;
    private Set<Cookie> cookies;

    public GatewayRequest(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
        this.queryStringDecoder = new QueryStringDecoder(fullHttpRequest.uri());
        this.requestBuilder = new RequestBuilder().setMethod(fullHttpRequest.method().name()).setQueryParams(queryStringDecoder.parameters()).setHeaders(fullHttpRequest.headers()).setBody(fullHttpRequest.content().nioBuffer());
    }

    public RequestBuilder builder() {
        return requestBuilder;
    }

    public Set<Cookie> cookies() {
        if (cookies == null) {
            if (headers().get(HttpHeaderNames.COOKIE) != null) {
                cookies = ServerCookieDecoder.STRICT.decode(headers().get(HttpHeaderNames.COOKIE));
            } else {
                cookies = new HashSet<>();
            }
        }
        return cookies;
    }

    public HttpHeaders headers() {
        return fullHttpRequest.headers();
    }

    public String path() {
        return queryStringDecoder.path();
    }

    public Map<String, List<String>> queryParameters() {
        return queryStringDecoder.parameters();
    }

    public HttpMethod method() {
        return fullHttpRequest.method();
    }

    public String host() {
        return headers().get(HttpHeaderNames.HOST);
    }

}
