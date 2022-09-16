package com.nacos.filter;

import com.alibaba.fastjson2.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * error过滤器是在服务网关出现异常的时候起作用的
 */
@Component
public class ErrorZuulFilter extends ZuulFilter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        //需要在默认的 SendErrorFilter 之前
        return 5;
    }

    @Override
    public boolean shouldFilter() {
//        String str = null;
//        str.length();
        // 只有在抛出异常时才会进行拦截
        boolean flag = RequestContext.getCurrentContext().containsKey("throwable");
        return flag;
    }

    @Override
    public Object run() {
//        String str = null;
//        str.length();
        try {
            RequestContext requestContext = RequestContext.getCurrentContext();
            Object e = requestContext.get("throwable");

            if (e instanceof ZuulException) {
                ZuulException zuulException = (ZuulException) e;
                // 删除该异常信息,不然在下一个过滤器中还会被执行处理
                requestContext.remove("throwable");
                // 响应给客户端信息
                HttpServletResponse response = requestContext.getResponse();
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pw = null;
                pw = response.getWriter();
                pw.write(JSONObject.toJSONString("系统出现异常"));
                pw.close();
            }
        } catch (Exception ex) {
            log.error("Exception filtering in custom error filter", ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }
}
