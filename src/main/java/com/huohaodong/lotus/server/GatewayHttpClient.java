package com.huohaodong.lotus.server;

import org.asynchttpclient.AsyncHttpClient;

public class GatewayHttpClient {

    private static final GatewayHttpClient INSTANCE = new GatewayHttpClient();

    private AsyncHttpClient asyncHttpClient;

    private GatewayHttpClient() {}

    public static GatewayHttpClient getInstance() {
        return INSTANCE;
    }

    public AsyncHttpClient client() {
        return INSTANCE.asyncHttpClient;
    }

    public void setClient(AsyncHttpClient client) {
        INSTANCE.asyncHttpClient = client;
    }
}
