package com.sw.uac.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 忽略权限的url
 * @Author: yu.leilei
 * @Date: 下午 1:30 2018/5/25 0025
 */
@Configuration
@ConfigurationProperties(prefix = "jwt.ignored")
public class IgnoredUrlsProperties {
    private List<String> urls = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
