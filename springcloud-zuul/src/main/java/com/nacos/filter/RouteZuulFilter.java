package com.nacos.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * post过滤器可以在请求转发后获取请求信息和响应，这里给大家写一个例子，可以用来记录请求日志
 */
@Component
public class RouteZuulFilter extends ZuulFilter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
//        String str = null;
//        str.length();
        // 进行跨域请求的时候，并且请求头中有额外参数，比如token，客户端会先发送一个OPTIONS请求来探测后续需要发起的跨域POST请求是否安全可接受
        // 所以这个请求就不需要拦截，下面是处理方式
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            log.info("OPTIONS请求不做拦截操作");
            return false;
        }
        // 如果前面的拦截器不进行路由，那么后面的过滤器就没必要执行，需要搭配 requestContext.setSendZuulResponse(false)，见 PreZuulFilter 类
        if (!requestContext.sendZuulResponse()) {
            return false;
        }
        // todo 暂时不执行该方法
        return false;
    }

    @SneakyThrows
    @Override
    public Object run() throws ZuulException {
//        String str = null;
//        str.length(); // 出错后会执行 ErrorZuulFilter 类
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String name = request.getParameter("name");
        if ("1234".equals(name)){ // 当参数 name 为1234 时，转发到 app-provider/hello2/app/1
            ctx.set("requestURI", "/hello2/app/1");
            ctx.set("proxy", "app-provider");
            ctx.set("serviceId", "app-provider");
        }
        return null;
    }
}
