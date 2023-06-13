package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

import java.util.Objects;

public class HeaderRoutePredicate extends SimpleKeyValuePredicate {

    public HeaderRoutePredicate(PredicateDefinition predicateDefinition) {
        super(predicateDefinition);
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return gatewayContext.getRequest().headers().contains(key()) && Objects.equals(gatewayContext.getRequest().headers().get(key()), value());
    }
}
