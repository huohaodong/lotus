package com.huohaodong.lotus.filter.factory;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.ratelimit.TokenBucketRateLimiterFilter;

import static com.huohaodong.lotus.filter.Constants.RATE_LIMITER_TOKEN_BUCKET_FILTER;

public class TokenBucketRateLimiterFilterFactory implements GatewayFilterFactory {
    @Override
    public GatewayFilter createFilter(FilterDefinition filterDefinition) {
        return new TokenBucketRateLimiterFilter(filterDefinition);
    }

    @Override
    public String getName() {
        return RATE_LIMITER_TOKEN_BUCKET_FILTER;
    }
}
