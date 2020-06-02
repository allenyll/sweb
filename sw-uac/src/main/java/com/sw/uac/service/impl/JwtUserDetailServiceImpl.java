package com.sw.uac.service.impl;

import com.sw.cache.util.SpringContextHolder;
import com.sw.common.entity.user.User;
import com.sw.uac.factory.JwtUserFactory;
import com.sw.uac.service.IUserAuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 提供一种从用户名可以查到用户并返回的方法
 * @Author: yu.leilei
 * @Date: 下午 4:25 2018/5/24 0024
 */
@Service("jwtUserDetailService")
public class JwtUserDetailServiceImpl implements UserDetailsService {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        IUserAuthService userAuthService = SpringContextHolder.getBean(IUserAuthService.class);
        User sysUser = userAuthService.getSysUser(username);

        if (sysUser == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(sysUser);
        }
    }
}
