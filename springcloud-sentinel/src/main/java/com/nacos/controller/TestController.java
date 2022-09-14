package com.nacos.controller;

import com.nacos.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public String test1() throws InterruptedException  {
        return helloService.hello();
    }
}
