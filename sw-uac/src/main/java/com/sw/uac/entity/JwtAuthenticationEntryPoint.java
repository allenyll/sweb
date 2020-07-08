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
import java.io.PrintWriter;
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
            header.put(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), authException.getMessage());
            result.put("HEADER",header);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, JSONObject.toJSONString(result));
        }else{
            header.put("ERROR_CODE","8001");
            header.put("ERROR_INFO","无效的token");
            header.put(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), authException.getMessage());
            result.put("HEADER",header);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, JSONObject.toJSONString(result));
        }
        System.out.println("认证失败：" + authException.getMessage());
//        response.setStatus(200);
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        PrintWriter printWriter = response.getWriter();
//        String body = JSONObject.toJSONString(result);
//        printWriter.write(body);
//        printWriter.flush();
    }
}
