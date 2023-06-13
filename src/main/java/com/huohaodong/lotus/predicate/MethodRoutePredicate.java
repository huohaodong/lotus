package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MethodRoutePredicate implements RoutePredicate {

    Set<HttpMethod> methods = new HashSet<>();

    public MethodRoutePredicate(PredicateDefinition predicateDefinition) {
        for (String method : predicateDefinition.getArgs().split(",")) {
            methods.add(HttpMethod.valueOf(method.toUpperCase()));
        }
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return methods.stream().anyMatch(method -> Objects.equals(gatewayContext.getRequest().method().name(), method.name()));
    }

}
