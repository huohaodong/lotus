package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class CookieRoutePredicate implements RoutePredicate {

    private String cookieName = "";

    private String cookieValue = "";

    public CookieRoutePredicate(PredicateDefinition predicateDefinition) {
        String[] args = predicateDefinition.getArgs().split(",");
        if (args.length > 2 || args.length == 0) {
            log.error("Malformed CookieRoutePredicate, must be in form of comma-split key value pair");
        }
        cookieName = args[0];
        cookieValue = args[1];
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        Optional<Cookie> cookie = gatewayContext.getRequest().cookies().stream().filter(c -> Objects.equals(c.name(), cookieName)).findFirst();
        return cookie.filter(value -> Objects.equals(value.value(), cookieValue)).isPresent();
    }
}
