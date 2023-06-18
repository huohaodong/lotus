package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.filter.factory.AuthFilterFactory;
import com.huohaodong.lotus.filter.factory.GatewayFilterFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import static com.huohaodong.lotus.filter.Constants.AUTH_FILTER;

@Slf4j
public class GatewayFilterManager {

    private final Map<String, GatewayFilterFactory> factories = new ConcurrentHashMap<>();

    private GatewayFilterManager() {
        loadDefault();
        loadFromSPI();
    }

    public static GatewayFilterManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void loadFromSPI() {
        ServiceLoader<GatewayFilterFactory> filterFactories = ServiceLoader.load(GatewayFilterFactory.class);
        for (GatewayFilterFactory factory : filterFactories) {
            factories.put(factory.getName(), factory);
        }
    }

    private void loadDefault() {
        factories.put(AUTH_FILTER, new AuthFilterFactory());
        // TODO: 实现 RateLimiterFactory
    }

    public GatewayFilter get(FilterDefinition filterDefinition) {
        String filterName = filterDefinition.getName();
        if (!factories.containsKey(filterName)) {
            log.error("Cannot find corresponding filter factory: {}", filterName);
            return null;
        }
        return factories.get(filterName).createFilter(filterDefinition);
    }

    private static class SingletonHolder {
        public static GatewayFilterManager INSTANCE = new GatewayFilterManager();
    }
}
