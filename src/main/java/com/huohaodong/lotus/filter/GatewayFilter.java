package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.server.context.GatewayContext;

public interface GatewayFilter {

    void filter(GatewayContext context, GatewayFilterChain chain);

}
