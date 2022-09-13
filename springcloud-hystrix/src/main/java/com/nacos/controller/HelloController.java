package com.nacos.controller;

import com.nacos.model.User;
import com.nacos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private UserService userService;

    /**
     * 服务容错测试
     * @return
     */
    @RequestMapping("/hello/user/hyx")
    @ResponseBody
    public User hello2() {
        return userService.hello("HystrixCommand");
    }
}

