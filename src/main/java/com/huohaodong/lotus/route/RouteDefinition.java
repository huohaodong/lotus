package com.huohaodong.lotus.route;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.predicate.PredicateDefinition;
import com.huohaodong.lotus.server.properties.RouteHystrixProperty;
import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
public class RouteDefinition {

    private String id;

    private URI uri;

    private int order = 0;

    private List<PredicateDefinition> predicates;

    private List<FilterDefinition> filters;

    private RouteHystrixProperty hystrixProperty;

}
