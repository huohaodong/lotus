package com.huohaodong.lotus.predicate;

import com.huohaodong.lotus.server.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public class HostRoutePredicate implements RoutePredicate {

    private final String pattern;

    public HostRoutePredicate(PredicateDefinition predicateDefinition) {
        String[] args = predicateDefinition.getArgs().split(":");
        if (args.length > 2 || args.length == 0) {
            log.error("Malformed RoutePredicate, must be in form of colon-split key value pair");
        }
        URI uri;
        if (args.length == 3) {
            uri = URI.create(predicateDefinition.getArgs());
            pattern = uri.getHost() + ":" + uri.getPort();
        } else if (args.length == 2) {
            pattern = args[0] + ":" + args[1];
        } else {
            pattern = args[0];
        }
    }

    @Override
    public boolean test(GatewayContext gatewayContext) {
        return pattern.equals(gatewayContext.getRequest().host());
    }

}
