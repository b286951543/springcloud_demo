package com.nacos.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson2.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * zuul 的过滤器的生命周期有四个阶段 pre、route、post和error
 *
 * PRE：可以在请求被路由之前调用，PRE过滤器用于将请求路径与配置的路由规则进行匹配，以找到需要转发的目标地址，并做一些前置加工，比如请求的校验等；
 *
 * ROUTING：路由请求时被调用，ROUTING过滤器用于将外部请求转发到具体服务实例上去；
 *
 * POST：在routing和error过滤器之后被调用，POST过滤器用于将微服务的响应信息返回到客户端，这个过程中可以对返回数据进行加工处理；
 *
 * ERROR：处理请求时发生错误时被调用，上述的过程发生异常后将调用ERROR过滤器。ERROR过滤器捕获到异常后需要将异常信息返回给客户端，所以最终还是会调用POST过滤器。
 *
 * 执行流程
 * ![01302.excalidraw.png](https://raw.githubusercontent.com/b286951543/resources/main/img2/01302.excalidraw.png)
 */
@Component
public class PreZuulFilter extends ZuulFilter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 对应Zuul生命周期的四个阶段：pre、route、post和error
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器的优先级，数字越小，优先级越高；
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 方法返回boolean类型，true时表示是否执行该过滤器的run方法，false则表示不执行；
     * @return
     */
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
        return true;
    }

    /**
     * 过滤器的过滤逻辑
     * @return
     */
    @Override
    public Object run() {
//        String str = null;
//        str.length(); // 出错后会执行 ErrorZuulFilter 类
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
//        String userToken = request.getHeader("apikey");
//        if (StringUtils.isBlank(userToken)) {
//            log.warn("apikey为空");
//            sendError(requestContext, 99001, "请传输参数apikey", null);
//            return null;
//        }
        String name = request.getParameter("name");
        if (StringUtils.isBlank(name)) {
            log.warn("参数为空");
            sendError(requestContext, 99001, "参数为空", null);
            return null;
        }

        String host = request.getRemoteHost();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("请求URI：{}，HTTP Method：{}，请求IP：{}", uri, method, host);
        return null;
    }

    /**
     * 发送错误消息
     * todo: 如果是重定向到本地 还是会正常返回对应的api数据，状态码会变成返回的状态码
     * todo: 如果是转发到其它服务的， 会被拦截下来返回对应的错误信息
     *
     * @param requestContext
     * @param status
     * @param msg
     */
    private void sendError(RequestContext requestContext, int status, String msg, String userToken) {
        requestContext.setSendZuulResponse(false); //不再对该请求进行转发
        requestContext.setResponseStatusCode(status);//设置返回状态码
        requestContext.setResponseBody(JSONObject.toJSONString(msg));//设置返回响应体
        requestContext.getResponse().setContentType("application/json;charset=UTF-8");//设置返回响应体格式，可能会乱码
    }
}
