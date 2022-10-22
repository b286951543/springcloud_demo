package com.nacos.api;

import com.nacos.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// value 的值对应服务提供者
// fallback 默认使用 hystrix 降级，添加 sentinel 依赖后，并且配置后，则使用 sentinel 降级
@FeignClient(value = "app-provider", fallback = OpenFeignServiceFallBack.class)
public interface OpenFeignService {
    // post请求
    @PostMapping("/openfeign/provider/order2")
    User getUser(@RequestBody User user);
}
