package com.huohaodong.lotus.predicate;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class PredicateDefinition {

    private String name;

    private Map<String, String> args = new LinkedHashMap<>();

}
