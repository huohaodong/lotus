package com.huohaodong.lotus.server;

public interface GatewayConfig {
    /**
     * Netty 相关配置
     */
    String host = "0.0.0.0";
    int port = 10086;
    int maxContentLength = 512 * 1024 * 1024;
    int heartBeatTimeoutSecond = 60 * 10;
}
