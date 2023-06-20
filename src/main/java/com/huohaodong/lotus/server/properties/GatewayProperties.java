package com.huohaodong.lotus.server.properties;

import cn.hutool.core.lang.Assert;
import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.route.RouteDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
public class GatewayProperties {
    /**
     * Netty 相关配置
     */
    private String host = "0.0.0.0";

    private int port = 10086;

    private int maxContentLength = 128 * 1024 * 1024;

    /**
     * Async Http Client 相关配置
     */
    private int heartBeatTimeoutSecond = 60 * 10;

    private int connectTimeout = 15 * 1000;

    private int requestTimeout = 10 * 1000;

    private int maxRedirects = 3;

    private int maxRequestRetry = 3;

    private int maxConnections = 100000;

    private int maxConnectionsPerHost = 10000;

    private int pooledConnectionIdleTimeout = 60 * 1000;

    /**
     * Redis 相关配置
     */
    private String redisHost;

    private int redisPort;

    /**
     * 路由相关
     */
    private List<RouteProperties> routes = new ArrayList<>();

    public List<RouteDefinition> populate() {
        return getRoutes().stream().map(Converter::convert).toList();
    }

    private static class Converter {
        private static RouteDefinition convert(RouteProperties routeProperties) {
            RouteDefinition routeDefinition = new RouteDefinition();
            Assert.notNull(routeProperties.getId());
            Assert.notNull(routeProperties.getUri());
            routeDefinition.setId(routeProperties.getId());
            routeDefinition.setUri(routeProperties.getUri());
            routeDefinition.setOrder(routeProperties.getOrder());
            List<PredicateDefinition> predicateDefinitions = routeProperties.getPredicates().stream().map(s -> {
                int eqIndex = s.indexOf("=");
                if (eqIndex <= 0) {
                    log.error("Unable to parse RouteDefinition for '" + s + "', must in the form of key=value");
                }
                return new PredicateDefinition(s.substring(0, eqIndex), s.substring(eqIndex + 1));
            }).toList();
            List<FilterDefinition> filterDefinitions = routeProperties.getFilters().stream().map(s -> {
                int eqIndex = s.indexOf("=");
                if (eqIndex <= 0) {
                    log.error("Unable to parse RouteDefinition for '" + s + "', must in the form of key=value");
                }
                return new FilterDefinition(s.substring(0, eqIndex), s.substring(eqIndex + 1));
            }).toList();
            routeDefinition.setPredicates(predicateDefinitions);
            routeDefinition.setFilters(filterDefinitions);
            return routeDefinition;
        }
    }
}
