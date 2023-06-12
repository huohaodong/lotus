package com.huohaodong.lotus.predicate.factory;

import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.predicate.RoutePredicate;

public interface RoutePredicateFactory {

    RoutePredicate createRoutePredicate(PredicateDefinition predicateDefinition);

}
