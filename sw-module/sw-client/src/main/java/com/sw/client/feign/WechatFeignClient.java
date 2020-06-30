package com.sw.client.feign;

import com.sw.client.fallback.WechatFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.util.DataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:  微信模块开放接口
 * @Author:       allenyll
 * @Date:         2020/5/10 11:50 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.WECHAT_SERVICE, fallbackFactory = WechatFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface WechatFeignClient {

    @RequestMapping(value = "/wx/auth", method = RequestMethod.POST)
    DataResponse auth(@RequestParam String code);
}
