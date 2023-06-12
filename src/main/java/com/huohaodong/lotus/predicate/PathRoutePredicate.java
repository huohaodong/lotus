package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

public class PathRoutePredicate implements RoutePredicate {
    //TODO: 实现 PathRoutePredicate，路径匹配可以使用 AntPathMatcher
    @Override
    public boolean test(GatewayContext gatewayContext) {
        return false;
    }
}
