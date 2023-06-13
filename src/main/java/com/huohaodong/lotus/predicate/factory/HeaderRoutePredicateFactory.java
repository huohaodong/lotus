package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.HeaderRoutePredicate;
import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;


public class HeaderRoutePredicateFactory implements RoutePredicateFactory {
    private HeaderRoutePredicateFactory() {
    }

    public static HeaderRoutePredicateFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        return new HeaderRoutePredicate(predicateDefinition);
    }

    private static class SingletonHolder {
        private static final HeaderRoutePredicateFactory INSTANCE = new HeaderRoutePredicateFactory();
    }
}

