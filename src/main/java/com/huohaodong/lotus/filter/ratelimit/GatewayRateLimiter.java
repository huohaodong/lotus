package com.huohaodong.lotus.filter.ratelimit;

public interface GatewayRateLimiter {
    boolean tryAcquire();
}
