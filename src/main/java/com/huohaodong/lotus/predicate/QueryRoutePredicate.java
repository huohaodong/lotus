package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryRoutePredicate implements RoutePredicate {
    Set<String> queries = new HashSet<>();

    public QueryRoutePredicate(PredicateDefinition predicateDefinition) {
        queries.addAll(List.of(predicateDefinition.getArgs().split(",")));
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return gatewayContext.getRequest().queryParameters().keySet().containsAll(queries);
    }
}
