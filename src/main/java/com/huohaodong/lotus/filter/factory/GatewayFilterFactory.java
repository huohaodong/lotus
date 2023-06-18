package com.huohaodong.lotus.filter.factory;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.filter.GatewayFilter;

public interface GatewayFilterFactory {

    GatewayFilter createFilter(FilterDefinition filterDefinition);

    String getName();

}
