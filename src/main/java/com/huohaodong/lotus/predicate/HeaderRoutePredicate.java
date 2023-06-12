package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HeaderRoutePredicate implements RoutePredicate {

    private final Set<String> headers = new HashSet<>();

    public HeaderRoutePredicate(PredicateDefinition predicateDefinition) {
        headers.addAll(Arrays.asList(predicateDefinition.getArgs().split(",")));
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return headers.stream().anyMatch(header -> gatewayContext.getRequest().headers().contains(header));
    }
}
