package com.huohaodong.lotus.predicate;

import cn.hutool.core.text.AntPathMatcher;
import com.huohaodong.lotus.server.context.GatewayContext;

public class PathRoutePredicate implements RoutePredicate {

    private static final AntPathMatcher INSTANCE = new AntPathMatcher();

    private final AntPathMatcher matcher = PathRoutePredicate.INSTANCE;

    private final String pattern;

    public PathRoutePredicate(PredicateDefinition predicateDefinition) {
        pattern = predicateDefinition.getArgs();
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return matcher.match(pattern, gatewayContext.getRequest().path());
    }

}
