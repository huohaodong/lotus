package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.request.GatewayRequest;

import static com.huohaodong.lotus.filter.Constant.defaultGatewayFilterOrder;

public interface GatewayFilter extends Ordered {

    void filter(GatewayRequest request, GatewayFilterChain chain);

    @Override
    default int getOrder() {
        GatewayFilterDefinition gatewayFilterAspect = this.getClass().getAnnotation(GatewayFilterDefinition.class);
        if (gatewayFilterAspect != null) {
            return gatewayFilterAspect.order();
        }
        return defaultGatewayFilterOrder;
    }
}
