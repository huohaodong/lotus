package com.huohaodong.lotus.server.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RouteProperties {

    private String id;

    private URI uri;

    private int order = 0;

    private List<String> predicates = new ArrayList<>();

    private List<String> filters = new ArrayList<>();

    private RouteHystrixProperty hystrix = new RouteHystrixProperty();

}
