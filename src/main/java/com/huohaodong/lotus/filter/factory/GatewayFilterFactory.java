package com.huohaodong.lotus.filter.factory;

import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.filter.GatewayFilter;

public interface GatewayFilterFactory {

    GatewayFilter createFilter(GatewayContext gatewayContext);

}
