package com.huohaodong.lotus.filter;

import com.huohaodong.lotus.server.context.GatewayContext;
import com.huohaodong.lotus.server.context.GatewayContextAttributes;
import lombok.Getter;

import java.util.List;

public class DefaultGatewayFilterChain implements GatewayFilterChain {

    private final int index;

    @Getter
    private final List<GatewayFilter> filters;

    public DefaultGatewayFilterChain(List<GatewayFilter> filters) {
        this.index = 0;
        this.filters = filters;
    }

    private DefaultGatewayFilterChain(DefaultGatewayFilterChain parent, int index) {
        this.filters = parent.getFilters();
        this.index = index;
    }

    @Override
    public void filter(GatewayContext ctx) {
        if (index < filters.size()) {
            GatewayFilter filter = filters.get(this.index);
            DefaultGatewayFilterChain chain = new DefaultGatewayFilterChain(this, this.index + 1);
            filter.filter(ctx, chain);
        } else {
            ctx.attributes().put(GatewayContextAttributes.FILTER_STATE, GatewayContextAttributes.FilterState.COMPLETED);
        }
    }
}
