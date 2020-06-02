package com.sw.client.config;

import com.sw.client.annotion.CurrentUser;
import com.sw.client.feign.UserFeignClient;
import com.sw.common.constants.SecurityConstants;
import com.sw.common.entity.user.User;
import com.sw.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:  CurrentUserMethodArgumentResolver
 * @Author:       allenyll
 * @Date:         2020/6/1 8:14 下午
 * @Version:      1.0
 */
@Slf4j
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private UserFeignClient userFeignClient;

    public CurrentUserMethodArgumentResolver(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        CurrentUser currentUser = parameter.getParameterAnnotation(CurrentUser.class);
        boolean isFull = currentUser.isFull();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String userName = request.getHeader(SecurityConstants.USER_HEADER);
        if (StringUtil.isEmpty(userName)) {
            log.warn("username is empty where execute resolveArgument");
            return null;
        }
        User user;
        if (isFull) {
            user = userFeignClient.selectUserByName(userName);
        } else {
            user = new User();
            user.setUserName(userName);
        }
        return user;
    }
}
