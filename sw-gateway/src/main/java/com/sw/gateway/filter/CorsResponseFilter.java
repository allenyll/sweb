//package com.sw.gateway.filter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//@Slf4j
//@Component
//public class CorsResponseFilter extends ZuulFilter {
//
//    /**
//     *返回布尔值来判断该过滤器是否要执行。可以通过此方法来执行过滤器的有效范围
//     */
//    @Override
//    public boolean shouldFilter() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        //只过滤OPTIONS 请求
//        if(request.getMethod().equals(RequestMethod.OPTIONS.name())){
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 具体逻辑
//     */
//    @Override
//    public Object run() {
//        log.debug("*****************FirstFilter run start*****************");
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletResponse response = ctx.getResponse();
//        HttpServletRequest request = ctx.getRequest();
//        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Credentials","true");
//        response.setHeader("Access-Control-Allow-Headers","authorization, content-type");
//        response.setHeader("Access-Control-Allow-Methods","POST,GET");
//        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
//        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
//        //不再路由
//        ctx.setSendZuulResponse(false);
//        ctx.setResponseStatusCode(200);
//        log.debug("*****************FirstFilter run end*****************");
//        return null;
//    }
//
//    /**
//     * 过滤器类型：
//     * pre: 在请求被路由之前调用
//     * route: 在路由请求时被调用
//     * post: 表示在route和error过滤器之后被调用
//     * error: 处理请求发生错误是被调用
//     */
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    /**
//     * 过滤器执行顺序，数值越小优先级越高，不同类型的过滤器，执行顺序的值可以相同
//     */
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//}
