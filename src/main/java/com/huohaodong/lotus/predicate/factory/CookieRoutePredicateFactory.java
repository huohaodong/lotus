package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;

public class CookieRoutePredicateFactory implements RoutePredicateFactory {
    private CookieRoutePredicateFactory() {
    }

    public static CookieRoutePredicateFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        return null;
    }

    private static class SingletonHolder {
        private static final CookieRoutePredicateFactory INSTANCE = new CookieRoutePredicateFactory();
    }
}
