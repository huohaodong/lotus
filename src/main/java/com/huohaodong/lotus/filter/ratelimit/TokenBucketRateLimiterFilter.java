package com.huohaodong.lotus.filter.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterChain;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;

public class TokenBucketRateLimiterFilter implements GatewayFilter {

    private final RateLimiter rateLimiter;

    public TokenBucketRateLimiterFilter(FilterDefinition filterDefinition) {
        String[] args = filterDefinition.getArgs().split(",");
        rateLimiter = RateLimiter.create(Double.parseDouble(args[0]));
    }

    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {
        if (rateLimiter.tryAcquire()) {
            chain.filter(context);
        } else {
            context.attributes().put(GatewayContextAttributes.FILTER_STATE, GatewayContextAttributes.FilterState.ABORT_TOO_MANY_REQUEST);
        }
    }

}
