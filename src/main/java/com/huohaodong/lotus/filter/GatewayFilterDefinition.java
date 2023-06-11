package com.huohaodong.lotus.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.huohaodong.lotus.filter.Constant.defaultGatewayFilterName;
import static com.huohaodong.lotus.filter.Constant.defaultGatewayFilterOrder;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GatewayFilterDefinition {

    String id();

    String name() default defaultGatewayFilterName;

    int order() default defaultGatewayFilterOrder;

}
