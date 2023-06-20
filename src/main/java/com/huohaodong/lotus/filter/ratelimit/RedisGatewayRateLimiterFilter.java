package com.huohaodong.lotus.filter.ratelimit;

// TODO: 基于 Redis + Lua 脚本实现分布式限流器
public class RedisGatewayRateLimiterFilter implements GatewayRateLimiter {
    @Override
    public boolean tryAcquire() {
        return false;
    }
}
