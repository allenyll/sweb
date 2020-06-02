package com.sw.uac.factory;

import com.sw.common.entity.user.User;
import com.sw.uac.entity.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 创建自定义JwtUser工厂，用户根据用户创建自定义JwtUser
 * @Author: yu.leilei
 * @Date: 下午 4:31 2018/5/24 0024
 */
public class JwtUserFactory {

    public JwtUserFactory() {
    }

    /**
     * 根据系统用户创建子订单jwtUser
     * @param user
     * @return
     */
    public static JwtUser create(User user) {
        return new JwtUser(
                user.getPkUserId(),
                user.getAccount(),
                user.getPassword(),
                null,
                user.getLastPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
