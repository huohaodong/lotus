package com.huohaodong.lotus.predicate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SimpleKeyValuePredicate implements RoutePredicate {
    private final String key;

    private final String value;

    public SimpleKeyValuePredicate(PredicateDefinition predicateDefinition) {
        String[] args = predicateDefinition.getArgs().split(",");
        if (args.length > 2 || args.length == 0) {
            log.error("Malformed RoutePredicate, must be in form of comma-split key value pair");
        }
        key = args[0];
        value = args.length > 1 ? args[1] : "";
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }
}
