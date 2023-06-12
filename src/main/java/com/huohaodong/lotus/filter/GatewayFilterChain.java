package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.server.context.GatewayContext;

public interface GatewayFilterChain {

    void filter(GatewayContext ctx);

}
