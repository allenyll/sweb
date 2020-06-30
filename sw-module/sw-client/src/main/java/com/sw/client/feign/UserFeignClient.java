package com.sw.client.feign;

import com.sw.client.fallback.UserFallbackFactory;
import com.sw.common.config.FeignConfiguration;
import com.sw.common.constants.FeignNameConstants;
import com.sw.common.entity.user.Dict;
import com.sw.common.entity.user.Log;
import com.sw.common.entity.user.SysUserRole;
import com.sw.common.entity.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:  feign 用户服务接口
 * @Author:       allenyll
 * @Date:         2020/5/4 5:54 下午
 * @Version:      1.0
 */
@FeignClient(name = FeignNameConstants.USER_SERVICE, fallbackFactory = UserFallbackFactory.class, configuration = FeignConfiguration.class, decode404 = true)
public interface UserFeignClient {

    @RequestMapping(value = "user/test", method = RequestMethod.POST)
    void test(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "user/add", method = RequestMethod.POST)
    void save(@RequestBody User userToAdd);

    @RequestMapping(value = "user/selectOne", method = RequestMethod.POST)
    User selectOne(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "user/selectOneSysUserRole", method = RequestMethod.POST)
    SysUserRole selectOneSysUserRole(@RequestBody Map<String, String> params);

    @RequestMapping(value = "user/getUserRoleMenuList", method = RequestMethod.POST)
    List<Map<String, Object>> getUserRoleMenuList(@RequestBody Map<String, String> param);

    @RequestMapping(value = "log/saveLog", method = RequestMethod.POST)
    void saveLog(@RequestBody Log sysLog);

    @RequestMapping(value = "user/selectById", method = RequestMethod.POST)
    User selectById(@RequestParam String userId);

    @RequestMapping(value = "dict/getDictByCode", method = RequestMethod.POST)
    Dict getDictByCode(@RequestParam String orderStatus);

    @RequestMapping(value = "user/selectUserByName", method = RequestMethod.POST)
    User selectUserByName(@RequestParam String userName);
}
