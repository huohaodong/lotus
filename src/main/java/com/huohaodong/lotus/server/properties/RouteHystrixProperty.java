package com.huohaodong.lotus.server.properties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteHystrixProperty {

    private boolean enable = false;

    private int threadCoreSize = 10;

    private int executionTimeoutInMilliseconds = 5000;

    private String fallback = "503 Service Unavailable";

}
