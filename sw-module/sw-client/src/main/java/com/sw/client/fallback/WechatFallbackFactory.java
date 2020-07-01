package com.sw.client.fallback;

import com.sw.client.feign.WechatFeignClient;
import com.sw.common.util.DataResponse;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:  sw-uac 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class WechatFallbackFactory implements FallbackFactory<WechatFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatFallbackFactory.class);

    @Override
    public WechatFeignClient create(Throwable throwable) {
        return new WechatFeignClient() {
            @Override
            public DataResponse auth(String code) {
                LOGGER.error("FEIGN调用：微信授权失败");
                return null;
            }
        };
    }
}
