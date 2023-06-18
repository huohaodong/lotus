package com.huohaodong.lotus.filter.factory;

import com.huohaodong.lotus.filter.AuthFilter;
import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;

import static com.huohaodong.lotus.filter.Constants.AUTH_FILTER;

public class AuthFilterFactory implements GatewayFilterFactory {
    @Override
    public GatewayFilter createFilter(FilterDefinition filterDefinition) {
        return new AuthFilter(filterDefinition);
    }

    @Override
    public String getName() {
        return AUTH_FILTER;
    }
}
