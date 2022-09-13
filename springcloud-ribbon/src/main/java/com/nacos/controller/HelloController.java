package com.nacos.controller;


import com.nacos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private RestTemplate restTemplate;

    // http://127.0.0.1:10005/hello/hello/user/1234
    @RequestMapping("/hello/user/{id:\\d+}")
    @ResponseBody
    public User hello1(@PathVariable String id) {
        Map<String, String> map = new HashMap<>();
        map.put("username", "java rest api -- " + id);
        User u = restTemplate.postForObject("http://app-provider/hello11/app/post", map, User.class);
//        String str = restTemplate.postForObject("http://app-provider/hello11/app/post", map, String.class);
        return u;
    }
}
