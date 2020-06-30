package com.sw.client.fallback;

import com.sw.client.feign.UacFeignClient;
import com.sw.common.entity.user.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.Result;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @Description:  sw-uac 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class UacFallbackFactory implements FallbackFactory<UacFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UacFallbackFactory.class);

    @Override
    public UacFeignClient create(Throwable throwable) {
        return new UacFeignClient() {
            @Override
            public DataResponse getUserInfo(String token) {
                LOGGER.error("调用sw-uac服务的getUserInfo方法失败");
                return null;
            }

            @Override
            public Result<User> getAuthentication(Map<String, String> param) {
                LOGGER.error("获取用户失败!");
                return null;
            }
        };
    }
}
