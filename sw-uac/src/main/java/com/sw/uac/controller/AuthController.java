package com.sw.uac.controller;

import com.sw.client.feign.WechatFeignClient;
import com.sw.common.entity.user.User;
import com.sw.common.entity.wechat.WxUserInfo;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.common.util.Result;
import com.sw.log.annotation.Log;
import com.sw.uac.entity.JwtAuthenticationRequest;
import com.sw.uac.entity.JwtAuthenticationResponse;
import com.sw.uac.service.IAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Api(value = "JWT权限认证接口", tags = "权限认证模块")
@RestController
@RequestMapping("uac")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.header}")
    private String tokenHeader;

    @Resource
    IAuthService authService;

    @Autowired
    WechatFeignClient wechatFeignClient;

    @ApiOperation(value = "测试用")
    @RequestMapping(value = "test", method = RequestMethod.POST)
    public DataResponse test(@RequestBody Map<String, Object> params) {
        authService.test(params);
        return DataResponse.success();
    }

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public User register(@RequestBody User addedUser) throws AuthenticationException{
        return authService.register(addedUser);
    }

    @Log(value = "系统登录")
    @ApiOperation(value = "根据用户名密码获取token")
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        // Return the token
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public String login(@RequestParam String username, @RequestParam String password){
        String token = authService.login(username, password);
        return token;
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @ResponseBody
    @ApiOperation(value = "根据token获取当前用户所属角色拥有的菜单", notes = "根据token获取当前用户所属角色拥有的菜单")
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public DataResponse getUserInfo(@RequestParam String token){
        return authService.getUserInfo(token);
    }

    @ResponseBody
    @RequestMapping(value = "getAuthentication", method = RequestMethod.POST)
    public Result<User> getAuthentication(@RequestBody Map<String, String> param) {
        Result<User> result= authService.getAuthentication(param);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("getAuthentication().authentication: ", authentication);
        return result;
    }

    @ApiOperation(value = "微信授权" ,  notes="微信授权")
    @RequestMapping(value = "/wx/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createWxAuthenticationToken(@RequestBody WxUserInfo user) throws AuthenticationException {
        DataResponse dataResponse = wechatFeignClient.auth(user.getCode());
        String token = MapUtil.getString(dataResponse, "token");
        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
}
