package com.sw.log.aspect;

import com.sw.client.feign.UserFeignClient;
import com.sw.common.entity.user.User;
import com.sw.common.util.*;
import com.sw.log.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Description:  日志切面
 * @Author:       allenyll
 * @Date:         2020-04-26 13:05
 * @Version:      1.0
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserFeignClient userFeignClient;

    @Pointcut("@annotation(com.sw.log.annotation.Log)")
    public void logPoint(){}

    @Around("logPoint()")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        LOGGER.info("执行切入点方法: {}", method.getName());
        long beginTime = System.currentTimeMillis();

        Object obj = null;

        try {
             obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error("执行切入方法异常");
            throwable.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        saveLog(joinPoint, endTime-beginTime);

        return obj;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);

        com.sw.common.entity.user.Log sysLog = new com.sw.common.entity.user.Log();

        if(log != null){
            sysLog.setOperation(log.value());
        }

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setClassName(className + "." + methodName + "()");

        Object[] args = joinPoint.getArgs();
        String userId = "";
        String account = "";
        if(args.length > 0){
            String params=  "";
            for(int i=0; i<args.length; i++){
                System.out.println(args[i]);
                if (args[i] instanceof User) {
                    User user = (User) args[i];
                    userId = user.getPkUserId();
                    account = user.getAccount();
                }
                params += "[" + JsonUtil.beanToJson(args[i]) + "], ";
            }
            if(StringUtil.isNotEmpty(params)){
                params = params.substring(0, params.length() - 2);
            }
            sysLog.setParams(params);
        }

        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        HttpServletResponse response = HttpContextUtils.getHttpServletResponse();
        // 设置IP地址
        sysLog.setIp(IPUtil.getIpAddr(request, response));

        sysLog.setFkUserId(userId);
        sysLog.setAccount(account);
        sysLog.setOperateTime(time);
        sysLog.setLogType(log.type());
        sysLog.setIsDelete(0);
        sysLog.setAddTime(DateUtil.getCurrentDateTime());
        sysLog.setAddUser(userId);
        sysLog.setUpdateTime(DateUtil.getCurrentDateTime());
        sysLog.setUpdateUser(userId);

        userFeignClient.saveLog(sysLog);
    }

}
