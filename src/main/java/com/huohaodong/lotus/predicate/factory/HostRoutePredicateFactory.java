package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.HostRoutePredicate;
import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;

public class HostRoutePredicateFactory implements RoutePredicateFactory {
    private HostRoutePredicateFactory() {
    }

    public static HostRoutePredicateFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        return new HostRoutePredicate(predicateDefinition);
    }

    private static class SingletonHolder {
        private static final HostRoutePredicateFactory INSTANCE = new HostRoutePredicateFactory();
    }
}
