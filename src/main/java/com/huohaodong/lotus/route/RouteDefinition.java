package com.huohaodong.lotus.route;

import com.huohaodong.lotus.filter.FilterDefinition;
import com.huohaodong.lotus.predicate.PredicateDefinition;
import lombok.Data;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RouteDefinition {

    private String id;

    private List<PredicateDefinition> predicates = new ArrayList<>();

    private List<FilterDefinition> filters = new ArrayList<>();

    private URI uri;

    private Map<String, Object> metadata = new HashMap<>();

    private int order = 0;

}
