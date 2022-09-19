package com.nacos.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jaeger")
public class HelloController {

    @RequestMapping("/user/{id:\\d+}")
    @ResponseBody
    public String hello(@PathVariable String id) {
        return id;
    }
}
