package com.sw.client.feign;

import com.sw.client.fallback.UserFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.user.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:  用户权限开放接口
 * @Author:       allenyll
 * @Date:         2020/5/10 11:50 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.UAC_SERVICE, fallbackFactory = UserFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface UacFeignClient {

    /**
     * @param token
     * @return
     */
    @RequestMapping(value = "uac/getUserInfo", method = RequestMethod.GET)
    DataResponse getUserInfo(@RequestParam String token);

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "uac/getAuthentication", method = RequestMethod.POST)
    Result<User> getAuthentication(@RequestBody Map<String, String> param);

}
