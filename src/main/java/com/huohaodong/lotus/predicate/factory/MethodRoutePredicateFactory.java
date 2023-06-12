package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;

public class MethodRoutePredicateFactory implements RoutePredicateFactory {
    private MethodRoutePredicateFactory() {
    }

    public static MethodRoutePredicateFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        return null;
    }

    private static class SingletonHolder {
        private static final MethodRoutePredicateFactory INSTANCE = new MethodRoutePredicateFactory();
    }
}
