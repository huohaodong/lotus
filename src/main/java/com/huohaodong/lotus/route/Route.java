package com.huohaodong.lotus.route;

import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.predicate.RoutePredicate;
import com.huohaodong.lotus.server.context.GatewayContext;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
public class Route implements Comparable<Route> {

    private final String id;

    private final URI uri;

    private final int order;

    private final List<RoutePredicate> routePredicates;

    private final List<GatewayFilter> filters;

    public boolean match(GatewayContext gatewayContext) {
        for (RoutePredicate predicate : routePredicates) {
            if (!predicate.test(gatewayContext)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(Route o) {
        return this.order - o.order;
    }
}
