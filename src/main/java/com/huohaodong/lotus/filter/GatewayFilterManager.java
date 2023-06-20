package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.filter.factory.GatewayFilterFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GatewayFilterManager {

    private final Map<String, GatewayFilterFactory> factories = new ConcurrentHashMap<>();

    private GatewayFilterManager() {
        loadFromSPI();
    }

    public static GatewayFilterManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void loadFromSPI() {
        ServiceLoader<GatewayFilterFactory> filterFactories = ServiceLoader.load(GatewayFilterFactory.class);
        for (GatewayFilterFactory factory : filterFactories) {
            log.info("Loader filter from spi: {}", factory.getName());
            factories.put(factory.getName(), factory);
        }
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
