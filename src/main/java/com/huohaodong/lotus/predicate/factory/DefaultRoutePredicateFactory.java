package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;

public class DefaultRoutePredicateFactory implements RoutePredicateFactory {

    private DefaultRoutePredicateFactory() {
    }

    public static DefaultRoutePredicateFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition) {
        switch (predicateDefinition.getType().toUpperCase()) {
            case "METHOD" -> {
                return MethodRoutePredicateFactory.getInstance().createRoutePredicate(predicateDefinition);
            }
            case "HEADER" -> {
                return HeaderRoutePredicateFactory.getInstance().createRoutePredicate(predicateDefinition);
            }
            case "HOST" -> {
                return HostRoutePredicateFactory.getInstance().createRoutePredicate(predicateDefinition);
            }
            case "PATH" -> {
                return PathRoutePredicateFactory.getInstance().createRoutePredicate(predicateDefinition);
            }
            case "QUERY" -> {
                return QueryRoutePredicateFactory.getInstance().createRoutePredicate(predicateDefinition);
            }
            case "COOKIE" -> {
                return CookieRoutePredicateFactory.getInstance().createRoutePredicate(predicateDefinition);
            }
            default ->
                    throw new RuntimeException("Cannot create predicate for unknown type: " + predicateDefinition.getType());
        }
    }

    private static class SingletonHolder {
        private static final DefaultRoutePredicateFactory INSTANCE = new DefaultRoutePredicateFactory();
    }
}
