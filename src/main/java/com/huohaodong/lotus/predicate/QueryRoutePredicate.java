package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

import java.util.List;

public class QueryRoutePredicate extends SimpleKeyValuePredicate {

    public QueryRoutePredicate(PredicateDefinition predicateDefinition) {
        super(predicateDefinition);
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        List<String> queries = gatewayContext.getRequest().queryParameters().get(key());
        return queries != null && queries.contains(value());
    }
}
