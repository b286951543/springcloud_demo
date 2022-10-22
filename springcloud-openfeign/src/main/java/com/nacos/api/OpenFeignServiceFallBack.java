package com.nacos.api;

import com.nacos.model.User;
import org.springframework.stereotype.Service;

@Service
public class OpenFeignServiceFallBack implements OpenFeignService{

    @Override
    public User getUser(User user) {
        System.out.println("调用 app-provider 服务失败");
        return null;
    }
}
