package com.sw.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sw.client.feign.UacFeignClient;
import com.sw.common.constants.SecurityConstants;
import com.sw.common.entity.user.User;
import com.sw.common.util.Result;
import com.sw.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UseInfoFilter extends ZuulFilter {

    @Resource
    UacFeignClient uacFeignClient;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request  = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        System.out.println("method: [" + request.getMethod() + "]");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
        }
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("auth: [" + header + "]");
        if (StringUtil.isNotEmpty(header)) {
            Result<User> result = uacFeignClient.getAuthentication(header);
            if (result.isSuccess()) {
                User user = result.getObject();
                if (user != null) {
                    ctx.addZuulRequestHeader(SecurityConstants.USER_HEADER, user.getUserName());
                    ctx.addZuulRequestHeader(SecurityConstants.USER_ID_HEADER, user.getPkUserId());
                }
            }
        }

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("authentication: {}", authentication);
//        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
//            Object object = authentication.getPrincipal();
//            String userName;
//            String userId;
//            if (object instanceof User) {
//                User user = (User) object;
//                userName = user.getUserName();
//                userId = user.getPkUserId();
//            } else {
//                userName = authentication.getName();
//                userId = "";
//            }
//        }
        return null;
    }
}
