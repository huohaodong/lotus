package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthFilter implements GatewayFilter {
    Set<String> tokens = new HashSet<>(List.of("Authorization"));

    public AuthFilter(FilterDefinition filterDefinition) {
        tokens.addAll(Arrays.asList(filterDefinition.getArgs().split(",")));
    }

    @Override
    public void filter(GatewayContext context, GatewayFilterChain chain) {
        if (tokens.stream().anyMatch(token -> context.getRequest().headers().contains(token))) {
            chain.filter(context);
        } else {
            context.attributes().put(GatewayContextAttributes.FILTER_STATE, GatewayContextAttributes.FilterState.ABORT_UNAUTHORIZED);
        }
    }
}
