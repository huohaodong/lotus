package com.huohaodong.lotus.filter.ratelimit;

import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterChain;
import com.huohaodong.lotus.server.context.GatewayContext;

// TODO: 实现令牌桶限流器
public class TokenBucketGatewayRateLimiterFilter implements GatewayRateLimiter, GatewayFilter {
    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {

    }
}
