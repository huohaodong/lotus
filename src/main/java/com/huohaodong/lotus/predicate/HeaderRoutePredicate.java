package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.predicate.RoutePredicate;
import com.huohaodong.lotus.server.context.GatewayContext;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

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
