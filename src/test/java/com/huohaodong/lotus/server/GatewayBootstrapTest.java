package com.huohaodong.lotus.server;

import cn.hutool.core.text.AntPathMatcher;
import com.huohaodong.lotus.server.properties.GatewayProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.net.URI;
import java.net.URISyntaxException;

class GatewayBootstrapTest {

    @Test
    void testLoadGatewayProperties() {
        Yaml yaml = new Yaml();
        GatewayProperties gatewayProperties = yaml.loadAs(this.getClass().getClassLoader().getResourceAsStream("GatewayProperties.yml"), GatewayProperties.class);
        System.out.println(gatewayProperties);
    }

    @Test
    void testAntPathMatcher() {
        String pattern = "/path/to/home/**";
        String path = "/path/to/home/1/2";
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCachePatterns(true);
        Assertions.assertTrue(matcher.match(pattern, path));
    }

    @Test
    void testLocalHost() throws URISyntaxException {
//        URI uri1 = new URI("http://localhost");
        URI uri2 = new URI("EMPTY://127.0.0.1:90");
        System.out.println(uri2.getHost());
    }
}