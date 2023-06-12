package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.server.context.GatewayContext;

public interface RoutePredicateFactory {

    GatewayFilter createRoutePredicate(GatewayContext gatewayContext);

}
