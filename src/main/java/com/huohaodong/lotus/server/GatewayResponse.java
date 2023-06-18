package com.huohaodong.lotus.server;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

public class GatewayResponse {

    public GatewayResponseBuilder builder() {
        return new GatewayResponseBuilder();
    }

    public class GatewayResponseBuilder {

        private HttpHeaders responseHeaders = EmptyHttpHeaders.INSTANCE;

        private HttpResponseStatus responseStatus;

        private String responseContent;

        private HttpVersion httpVersion;

        public GatewayResponseBuilder headers(HttpHeaders headers) {
            responseHeaders.add(headers);
            return this;
        }

        public GatewayResponseBuilder status(HttpResponseStatus status) {
            responseStatus = status;
            return this;
        }

        public GatewayResponseBuilder content(String content) {
            responseContent = content;
            return this;
        }

        public GatewayResponseBuilder httpVersion(HttpVersion version) {
            httpVersion = version;
            return this;
        }

        public FullHttpResponse build() {
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(httpVersion, responseStatus, Unpooled.wrappedBuffer(responseContent.getBytes()));
            fullHttpResponse.headers().add(responseHeaders);
            return fullHttpResponse;
        }

    }

}
