package com.huohaodong.lotus.route;

import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.predicate.RoutePredicate;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
public class Route {

    private final String id;

    private final URI uri;

    private final int order;

    private final RoutePredicate routePredicate;

    private final List<GatewayFilter> filters;

}
