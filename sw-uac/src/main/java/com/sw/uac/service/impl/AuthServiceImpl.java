package com.sw.uac.service.impl;

import com.sw.client.feign.CustomerFeignClient;
import com.sw.client.feign.UserFeignClient;
import com.sw.common.constants.BaseConstants;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.user.SysUserRole;
import com.sw.common.entity.user.User;
import com.sw.common.util.*;
import com.sw.uac.entity.JwtUser;
import com.sw.uac.service.IAuthService;
import com.sw.uac.service.IUserAuthService;
import com.sw.uac.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户操作接口实现
 * @Author: yu.leilei
 * @Date: 下午 3:11 2018/5/25 0025
 */
@Service("authService")
public class AuthServiceImpl implements IAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    UserDetailsService userDetailsService;

    @Autowired
    CustomerFeignClient customerFeignClient;

    @Resource
    JwtUtil jwtUtil;

    @Resource
    UserFeignClient userFeignClient;

    @Autowired
    IUserAuthService userAuthService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public User register(User userToAdd) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(passwordEncoder.encode(rawPassword));
        userToAdd.setLastPasswordResetDate(new Date());
        userFeignClient.save(userToAdd);
        return userToAdd;
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){

        }
        return null;
    }

    @Override
    public void test(Map<String, Object> params) {
        userFeignClient.test(params);
    }

    @Override
    public DataResponse getUserInfo(String token) {
        LOGGER.info("============= {token："+ token +"} =============");
        Map<String, Object> result = new HashMap<>();
        final String authToken = token.substring(tokenHead.length());
        String userName = jwtUtil.getUsernameFromToken(authToken);
        User user = userAuthService.getSysUser(userName);
        LOGGER.info("User.id: " + user.getPkUserId() + "user.name: " + userName);

        if(user != null){
            String userId = user.getPkUserId();
            Map<String, String> param = new HashMap<>();
            param.put("user_id", userId);
            // 根据userId查询用户角色
            List<Map<String, Object>> menuList = userFeignClient.getUserRoleMenuList(param);
            SysUserRole sysUserRole = userFeignClient.selectOneSysUserRole(param);
            result.put("user", user);
            result.put("menus", menuList);
            result.put("roles", sysUserRole);

        }else{
            LOGGER.info("没有查询到用户!");
            return DataResponse.fail("没有查询到该用户！");
        }
        return DataResponse.success(result);
    }

    @Override
    public Result<User> getAuthentication(Map<String, String> param) {
        Result<User> result = new Result<>();
        String token = MapUtil.getString(param, "HEADER");
        String loginType = MapUtil.getString(param, "LOGIN_TYPE");
        final String authToken = token.substring(tokenHead.length());
        if (StringUtil.isNotEmpty(authToken)) {
            if (StringUtil.isNotEmpty(loginType) && BaseConstants.SW_WECHAT.equals(loginType)) {
                String[] tokenArr = authToken.split("#");
                String openid = tokenArr[1];
                if(openid.equals(AppContext.getCurrentUserWechatOpenId())){
                    Map<String, Object> _map = new HashMap<>();
                    _map.put("MARK", BaseConstants.SW_WECHAT);
                    _map.put("OPENID", AppContext.getCurrentUserWechatOpenId());
                    Customer customer = customerFeignClient.selectOne(_map);
                    if (customer != null) {
                        User user= new User();
                        user.setPkUserId(customer.getPkCustomerId());
                        user.setUserName(customer.getCustomerName());
                        user.setAccount(customer.getCustomerAccount());
                        result.setObject(user);
                    }
                }
            } else {
                String userName = jwtUtil.getUsernameFromToken(authToken);
                // 根据account去数据库中查询user数据，足够信任token的情况下，可以省略这一步
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                if (jwtUtil.validateToken(authToken, userDetails)) {
                    User user = userAuthService.getSysUser(userName);
                    result.setObject(user);
                } else {
                    result.fail("token失效");
                    return result;
                }
            }
        } else {
            result.fail("token为空，验证失败");
            return result;
        }

        return result;
    }
}
