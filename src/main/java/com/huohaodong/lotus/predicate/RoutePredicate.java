package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface RoutePredicate extends Predicate<GatewayContext> {
    Map<String, Object> args = new HashMap<>();
}
