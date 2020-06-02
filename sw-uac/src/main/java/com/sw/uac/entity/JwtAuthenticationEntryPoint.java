package com.sw.uac.entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * jwt 未授权处理
 * @Author: yu.leilei
 * @Date: 下午 1:42 2018/5/25 0025
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable{

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject result = new JSONObject();
        JSONObject header = new JSONObject();
        /**身份认证未通过*/
        if(authException instanceof BadCredentialsException){
            header.put("ERROR_CODE","8002");
            header.put("ERROR_INFO","用户名或密码错误，请重新输入！");
            result.put("HEADER",header);
        }else{
            header.put("ERROR_CODE","8001");
            header.put("ERROR_INFO","无效的token");
            result.put("HEADER",header);
        }
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}
