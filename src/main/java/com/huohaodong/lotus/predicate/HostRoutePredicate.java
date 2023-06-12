package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

public class HostRoutePredicate implements RoutePredicate {

    String host = "";

    public HostRoutePredicate(PredicateDefinition predicateDefinition) {
        host = predicateDefinition.getArgs().toLowerCase();
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return gatewayContext.getRequest().host().equals(host);
    }

}
