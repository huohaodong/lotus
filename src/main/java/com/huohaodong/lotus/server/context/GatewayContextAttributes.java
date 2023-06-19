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

    /**
     * 上下文对应的 Route
     */
    String ROUTE = "ROUTE";

    /**
     * FilterChain 执行结果
     */
    String FILTER_STATE = "FILTER_STATE";

    enum FilterState {
        ABORT_UNAUTHORIZED,
        ABORT_TOO_MANY_REQUEST,
        COMPLETE
    }
}
