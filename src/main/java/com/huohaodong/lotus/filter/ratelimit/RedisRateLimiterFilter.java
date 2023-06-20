package com.huohaodong.lotus.filter.ratelimit;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;
import com.huohaodong.lotus.filter.GatewayFilterChain;
import com.huohaodong.lotus.route.Route;
import com.huohaodong.lotus.server.GatewayBootstrap;
import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import com.huohaodong.lotus.server.properties.GatewayProperties;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisRateLimiterFilter implements GatewayFilter {

    private static final GatewayProperties properties = GatewayBootstrap.gatewayProperties;

    private static final StatefulRedisConnection<String, String> connection = RedisClient.create(RedisURI.create(properties.getRedisHost(), properties.getRedisPort())).connect();

    private static final String LUA_SCRIPT =
            """
                    local current_time = redis.call('TIME')
                    local key = KEYS[1]
                    local max_requests = tonumber(ARGV[1])
                    local window = tonumber(ARGV[2])
                    local trim_time = tonumber(current_time[1]) - window
                    redis.call('ZREMRANGEBYSCORE', key, 0, trim_time)
                    local request_count = redis.call('ZCARD', key)

                    if request_count < tonumber(max_requests) then
                        redis.call('ZADD', key, current_time[1], current_time[1] .. current_time[2])
                        redis.call('EXPIRE', key, window)
                        return 0
                    end
                    return 1
                                                            """;

    private final int permits;

    /**
     * 滑动窗口的时间大小，单位：秒
     */
    private final int window;

    public RedisRateLimiterFilter(FilterDefinition filterDefinition) {
        String[] args = filterDefinition.getArgs().split(",");
        permits = Integer.parseInt(args[0]);
        window = Integer.parseInt(args[1]);
    }

    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {
        Route route = (Route) context.attributes().get(GatewayContextAttributes.ROUTE);
        String key = "lotus:" + route.getId();
        long res = connection.sync().eval(LUA_SCRIPT, ScriptOutputType.INTEGER, new String[]{key}, String.valueOf(permits), String.valueOf(window));
        if (res == 0) {
            chain.filter(context);
        } else {
            context.attributes().put(GatewayContextAttributes.FILTER_STATE, GatewayContextAttributes.FilterState.ABORT_TOO_MANY_REQUEST);
        }
    }
}
