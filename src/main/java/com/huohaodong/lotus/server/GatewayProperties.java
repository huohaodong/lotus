package com.huohaodong.lotus.server;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GatewayProperties {
    /**
     * Netty 相关配置
     */
    private String host = "0.0.0.0";

    private int port = 10086;

    private int maxContentLength = 128 * 1024 * 1024;

    /**
     * Async Http Client 相关配置
     */
    private int heartBeatTimeoutSecond = 60 * 10;

    private int connectTimeout = 15 * 1000;

    private int requestTimeout = 10 * 1000;

    private int maxRedirects = 3;

    private int maxRequestRetry = 3;

    private int maxConnections = 100000;

    private int maxConnectionsPerHost = 10000;

    private int pooledConnectionIdleTimeout = 60 * 1000;

    // TODO: 添加 Route 相关配置
}
