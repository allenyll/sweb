package com.sw.client.fallback;

import com.sw.client.feign.CmsFeignClient;
import com.sw.common.entity.cms.SearchHistory;
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
public class CmsFallbackFactory implements FallbackFactory<CmsFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsFallbackFactory.class);

    @Override
    public CmsFeignClient create(Throwable throwable) {
        return new CmsFeignClient() {
            @Override
            public void insert(SearchHistory searchHistory) {
                LOGGER.error("FEIGN调用：新增搜索记录失败");
            }
        };
    }
}
