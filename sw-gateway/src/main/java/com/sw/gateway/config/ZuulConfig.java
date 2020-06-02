package com.sw.gateway.config;

import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulConfig {

    //自定义 serviceId 和路由之间的相互映射
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
        return new PatternServiceRouteMapper(
                "(?<project>^.+)-(?<subProject>.+$)",
                "${project}/${subProject}");
    }

}
