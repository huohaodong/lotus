package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.RoutePredicate;
import com.huohaodong.lotus.server.context.GatewayContext;

public interface RoutePredicateFactory {

    RoutePredicate createRoutePredicate(GatewayContext gatewayContext);

}
