package com.nacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // 通过该注解去 nacos 获取，支持动态更新
public class ConfigController {
    @Value("${jzx.name:1234}") // 取不到配置时，使用 1234
    private String appName;

    @Value("${nacos1.name:1234}") // 取不到配置时，使用 1234
    private String name1;

    @Value("${nacos2.name:1234}") // 取不到配置时，使用 1234
    private String name2;

    @Value("${shared01.name:1234}") // 取不到配置时，使用 1234
    private String sharedName1;

    @Value("${shared02.name:1234}") // 取不到配置时，使用 1234
    private String sharedName2;

    // http://localhost:8003/api/appInfo
    @GetMapping("/api/appInfo")
    public String appInfo() {
        return "appName: " + appName + "," + name1 + "," + name2 + "。 " + sharedName1 + "," + sharedName2;
    }
}
