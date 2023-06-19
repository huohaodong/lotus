package com.huohaodong.lotus.server.context;

public interface GatewayContextAttributes {
    /**
     * HTTP 请求的用户 ip
     */
    String CLIENT_IP = "CLIENT_IP";

    /**
     * HTTP KeepAlive
     */
    String KEEP_ALIVE = "KEEP_ALIVE";

}
