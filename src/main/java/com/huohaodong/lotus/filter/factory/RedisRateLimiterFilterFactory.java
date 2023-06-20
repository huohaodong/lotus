package com.huohaodong.lotus.filter.factory;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.ratelimit.RedisRateLimiterFilter;

import static com.huohaodong.lotus.filter.Constants.RATE_LIMITER_REDIS_FILTER;

public class RedisRateLimiterFilterFactory implements GatewayFilterFactory {

    @Override
    public GatewayFilter createFilter(FilterDefinition filterDefinition) {
        return new RedisRateLimiterFilter(filterDefinition);
    }

    @Override
    public String getName() {
        return RATE_LIMITER_REDIS_FILTER;
    }
}
