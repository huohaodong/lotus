package com.huohaodong.lotus.handler;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterManager;
import com.huohaodong.lotus.filter.factory.GatewayFilterFactory;
import com.huohaodong.lotus.predicate.RoutePredicate;
import com.huohaodong.lotus.predicate.factory.DefaultRoutePredicateFactory;
import com.huohaodong.lotus.predicate.factory.RoutePredicateFactory;
import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.route.RouteDefinition;
import com.huohaodong.lotus.server.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class GatewayRouter {

    private final RoutePredicateFactory routePredicateFactory = DefaultRoutePredicateFactory.getInstance();

    private final GatewayFilterManager gatewayFilterManager = GatewayFilterManager.getInstance();

    private List<Route> routes = new ArrayList<>();

    private GatewayRouter() {
    }

    public static GatewayRouter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Optional<Route> route(GatewayContext gatewayContext) {
        for (Route route : routes) {
            if (route.match(gatewayContext)) {
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

    public void refresh(List<RouteDefinition> routeDefinitions) {
        routes = routeDefinitions.stream().map(routeDefinition -> {
            List<RoutePredicate> predicates = routeDefinition.getPredicates().stream()
                    .map(routePredicateFactory::createRoutePredicate)
                    .collect(Collectors.toList());

            // TODO: 根据 RouteDefinition 构造对应的 GatewayFilter
            List<GatewayFilter> filters = routeDefinition.getFilters().stream().map(gatewayFilterManager::get).toList();
            return new Route(routeDefinition.getId(), routeDefinition.getUri(), routeDefinition.getOrder(), predicates, filters);
        }).sorted().collect(Collectors.toCollection(ArrayList::new));
        log.info("Router refreshed with total {} Routes", routes.size());
    }

    private static class SingletonHolder {
        private static final GatewayRouter INSTANCE = new GatewayRouter();
    }

}
