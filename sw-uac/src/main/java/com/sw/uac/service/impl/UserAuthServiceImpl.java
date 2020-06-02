package com.sw.uac.service.impl;

import com.sw.client.feign.UserFeignClient;
import com.sw.common.constants.dict.UserStatus;
import com.sw.common.entity.user.User;
import com.sw.uac.service.IUserAuthService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户权限相关业务实现
 * @Author: yu.leilei
 * @Date: 下午 4:57 2018/5/24 0024
 */
@Service("userAuthService")
public class UserAuthServiceImpl implements IUserAuthService {

    @Resource
    UserFeignClient userFeignClient;

    @Override
    @Cacheable(value= "SYS_AUTH", key="#account+'_USER'")
    public User getSysUser(String userName) {
        Map<String, Object> map = new HashMap<>();
        map.put("ACCOUNT", userName);
        map.put("STATUS", UserStatus.OK.getCode());
        map.put("IS_DELETE", 0);
        User sysUser = userFeignClient.selectOne(map);
        return sysUser;
    }
}
