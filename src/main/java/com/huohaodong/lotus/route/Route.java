package com.huohaodong.lotus.route;

import com.huohaodong.lotus.filter.GatewayFilter;
import lombok.Data;

import java.util.List;

@Data
public class Route {
    private final String id;

    private final String uri;

    private final int order;

    private final RoutePredicate routePredicate;

    private final List<GatewayFilter> filters;
}
