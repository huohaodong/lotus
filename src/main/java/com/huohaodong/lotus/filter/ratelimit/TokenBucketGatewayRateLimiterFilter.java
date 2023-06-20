package com.huohaodong.lotus.filter.ratelimit;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterChain;
import com.huohaodong.lotus.server.context.GatewayContext;

// TODO: 实现令牌桶限流器
public class TokenBucketGatewayRateLimiterFilter implements GatewayRateLimiter, GatewayFilter {

    public TokenBucketGatewayRateLimiterFilter(FilterDefinition filterDefinition) {

    }

    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {

    }
}
