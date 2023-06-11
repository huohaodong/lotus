package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.request.GatewayRequest;

public interface GatewayFilterFactory {

    GatewayFilter createFilterChain(GatewayRequest gatewayRequest);
}
