package com.huohaodong.lotus.server;

import org.asynchttpclient.AsyncHttpClient;

public class GatewayHttpClient {

    private AsyncHttpClient asyncHttpClient;

    private GatewayHttpClient() {}

    public static GatewayHttpClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AsyncHttpClient client() {
        return asyncHttpClient;
    }

    public void setClient(AsyncHttpClient client) {
        this.asyncHttpClient = client;
    }

    private static class SingletonHolder {
        private static final GatewayHttpClient INSTANCE = new GatewayHttpClient();
    }
}
