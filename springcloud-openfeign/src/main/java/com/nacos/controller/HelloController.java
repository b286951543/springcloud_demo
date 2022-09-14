package com.nacos.controller;


import com.nacos.api.OpenFeignService;
import com.nacos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private OpenFeignService openFeignService;

    @RequestMapping("/hello/user/{id:\\d+}")
    @ResponseBody
    public User hello(@PathVariable String id) {
        User user = new User();
        user.setUsername(id);
        return openFeignService.getUser(user);
    }
}
