package com.huohaodong.lotus.handler;

import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterManager;
import com.huohaodong.lotus.predicate.RoutePredicate;
import com.huohaodong.lotus.predicate.factory.DefaultRoutePredicateFactory;
import com.huohaodong.lotus.predicate.factory.RoutePredicateFactory;
import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.route.RouteDefinition;
import com.huohaodong.lotus.server.GatewayBootstrap;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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

    public static FullHttpResponse RESPONSE_NOT_FOUND() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND,
                Unpooled.wrappedBuffer("404 Not Found".getBytes()));
    }

    public static FullHttpResponse RESPONSE_TOO_MANY_REQUEST() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.TOO_MANY_REQUESTS,
                Unpooled.wrappedBuffer("429 Too Many Request".getBytes()));
    }

    public static FullHttpResponse RESPONSE_UNAUTHORIZED() {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.UNAUTHORIZED,
                Unpooled.wrappedBuffer("401 Unauthorized".getBytes()));
    }

    public Optional<Route> match(GatewayContext context) {
        for (Route route : routes) {
            if (route.match(context)) {
                return Optional.of(route);
            }
        }
        return Optional.empty();
    }

    public void route(GatewayContext context) {
        Route route = (Route) context.attributes().get(GatewayContextAttributes.ROUTE);
        boolean isKeepAlive = (Boolean) context.attributes().getOrDefault(GatewayContextAttributes.KEEP_ALIVE, false);
        GatewayContextAttributes.FilterState filterState = (GatewayContextAttributes.FilterState) context.getAttributes().get(GatewayContextAttributes.FILTER_STATE);
        CompletableFuture<Response> future = GatewayBootstrap.asyncHttpClient.executeRequest(context.getRequest().builder().setUrl(String.valueOf(route.getUri())))
                .toCompletableFuture();
        future.whenComplete((response, throwable) -> {
            FullHttpResponse fullHttpResponse;
            if (response == null) {
                fullHttpResponse = RESPONSE_NOT_FOUND();
            } else {
                fullHttpResponse = switch (filterState) {
                    case COMPLETED -> context.getResponse().builder()
                            .headers(response.getHeaders())
                            .httpVersion(HttpVersion.HTTP_1_1)
                            .status(HttpResponseStatus.valueOf(response.getStatusCode()))
                            .content(response.getResponseBody())
                            .build();
                    case ABORT_UNAUTHORIZED -> RESPONSE_UNAUTHORIZED();
                    case ABORT_TOO_MANY_REQUEST -> RESPONSE_TOO_MANY_REQUEST();
                };
            }
            if (isKeepAlive) {
                context.getChannelHandlerContext().writeAndFlush(fullHttpResponse);
            } else {
                context.getChannelHandlerContext().writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
            }
        });
    }

    public void refresh(List<RouteDefinition> routeDefinitions) {
        routes = routeDefinitions.stream().map(routeDefinition -> {
            List<RoutePredicate> predicates = routeDefinition.getPredicates().stream()
                    .map(routePredicateFactory::createRoutePredicate)
                    .collect(Collectors.toList());
            List<GatewayFilter> filters = routeDefinition.getFilters().stream().map(gatewayFilterManager::get).toList();
            return new Route(routeDefinition.getId(), routeDefinition.getUri(), routeDefinition.getOrder(), predicates, filters, routeDefinition.getHystrixProperty());
        }).sorted().collect(Collectors.toCollection(ArrayList::new));
        log.info("Router refreshed with total {} Routes", routes.size());
    }

    private static class SingletonHolder {
        private static final GatewayRouter INSTANCE = new GatewayRouter();
    }

}
