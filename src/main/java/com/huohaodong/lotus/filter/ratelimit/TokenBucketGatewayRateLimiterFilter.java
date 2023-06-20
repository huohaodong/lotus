package com.huohaodong.lotus.filter.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterChain;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;

// TODO: 实现令牌桶限流器
public class TokenBucketGatewayRateLimiterFilter implements GatewayRateLimiter, GatewayFilter {

    private final RateLimiter rateLimiter;

    public TokenBucketGatewayRateLimiterFilter(FilterDefinition filterDefinition) {
        String[] args = filterDefinition.getArgs().split(",");
        rateLimiter = RateLimiter.create(Double.parseDouble(args[0]));
    }

    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {
        if (tryAcquire()) {
            chain.filter(context);
        } else {
            context.attributes().put(GatewayContextAttributes.FILTER_STATE, GatewayContextAttributes.FilterState.ABORT_TOO_MANY_REQUEST);
        }
    }

    @Override
    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }
}
