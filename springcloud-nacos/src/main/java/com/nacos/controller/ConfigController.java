package com.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 通过该注解去 nacos 获取，支持动态更新
public class ConfigController {
    @Value("${jzx.name:1234}")
    private String appName;

    @GetMapping("/api/appInfo")
    public String appInfo() {
        return "appName: " + appName;
    }
}
