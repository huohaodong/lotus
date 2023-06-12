package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;

public class PathRoutePredicateFactory implements RoutePredicateFactory {
    private PathRoutePredicateFactory() {
    }

    public static PathRoutePredicateFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        return null;
    }

    private static class SingletonHolder {
        private static final PathRoutePredicateFactory INSTANCE = new PathRoutePredicateFactory();
    }
}
