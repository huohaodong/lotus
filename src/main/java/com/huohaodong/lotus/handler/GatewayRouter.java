package com.huohaodong.lotus.handler;

import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.predicate.RoutePredicate;
import com.huohaodong.lotus.predicate.factory.DefaultRoutePredicateFactory;
import com.huohaodong.lotus.predicate.factory.RoutePredicateFactory;
import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.route.RouteDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class GatewayRouter {

    RoutePredicateFactory routePredicateFactory = DefaultRoutePredicateFactory.getInstance();
    private List<Route> routes = new ArrayList<>();

    private GatewayRouter() {
    }

    public void refresh(List<RouteDefinition> routeDefinitions) {
        routes = routeDefinitions.stream().map(new Function<RouteDefinition, Route>() {
            @Override
            public Route apply(RouteDefinition routeDefinition) {
                List<RoutePredicate> predicates = routeDefinition.getPredicates().stream()
                        .map(routePredicateFactory::createRoutePredicate)
                        .collect(Collectors.toList());

                // TODO: 根据 RouteDefinition 构造对应的 GatewayFilter
                List<GatewayFilter> filters = new ArrayList<>();

                return new Route(routeDefinition.getId(), routeDefinition.getUri(), routeDefinition.getOrder(), predicates, filters);
            }
        }).sorted().collect(Collectors.toList());
        log.info("Router refreshed with total {} Routes", routes.size());
    }

    public GatewayRouter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final GatewayRouter INSTANCE = new GatewayRouter();
    }

}
