package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CookieRoutePredicate implements RoutePredicate {

    private final Set<String> cookieTokens = new HashSet<>();

    public CookieRoutePredicate(PredicateDefinition predicateDefinition) {
        cookieTokens.addAll(Arrays.asList(predicateDefinition.getArgs().split(",")));
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(gatewayContext.getRequest().getFullHttpRequest().headers().get(HttpHeaderNames.COOKIE));
        return cookies.stream().anyMatch(cookie -> cookieTokens.contains(cookie.name()));
    }
}
