package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CookieRoutePredicate implements RoutePredicate {

    private final Set<String> cookies = new HashSet<>();

    public CookieRoutePredicate(PredicateDefinition predicateDefinition) {
        cookies.addAll(Arrays.asList(predicateDefinition.getArgs().split(",")));
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return gatewayContext.getRequest().cookies().stream().anyMatch(cookie -> cookies.contains(cookie.name()));
    }
}
