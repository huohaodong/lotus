package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.QueryRoutePredicate;
import com.huohaodong.lotus.predicate.RoutePredicate;

public class QueryRoutePredicateFactory implements RoutePredicateFactory {
    private QueryRoutePredicateFactory() {
    }

    public static QueryRoutePredicateFactory getInstance() {
        return QueryRoutePredicateFactory.SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        return new QueryRoutePredicate(predicateDefinition);
    }

    private static class SingletonHolder {
        private static final QueryRoutePredicateFactory INSTANCE = new QueryRoutePredicateFactory();
    }
}
