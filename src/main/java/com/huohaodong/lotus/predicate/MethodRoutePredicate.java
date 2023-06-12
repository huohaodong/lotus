package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;
import io.netty.handler.codec.http.HttpMethod;

public class MethodRoutePredicate implements RoutePredicate {

    HttpMethod method;

    public MethodRoutePredicate(PredicateDefinition predicateDefinition) {
        method = new HttpMethod(predicateDefinition.getArgs().toUpperCase());
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return gatewayContext.getRequest().method().equals(method);
    }

}
