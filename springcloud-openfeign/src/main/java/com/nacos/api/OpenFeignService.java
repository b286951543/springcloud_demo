package com.nacos.api;

import com.nacos.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "app-provider") // value 的值对应服务提供者
public interface OpenFeignService {
    // post请求
    @PostMapping("/openfeign/provider/order2")
    User getUser(@RequestBody User user);
}
