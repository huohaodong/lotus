package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;
import io.netty.handler.codec.http.cookie.Cookie;

import java.util.Objects;
import java.util.Optional;

public class CookieRoutePredicate extends SimpleKeyValuePredicate {

    public CookieRoutePredicate(PredicateDefinition predicateDefinition) {
        super(predicateDefinition);
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        Optional<Cookie> cookie = gatewayContext.getRequest().cookies().stream().filter(c -> Objects.equals(c.name(), key())).findFirst();
        return cookie.filter(value -> Objects.equals(value.value(), value())).isPresent();
    }
}
