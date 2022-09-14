package com.nacos.controller;

import com.nacos.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/")
public class HelloController {

    @GetMapping("/hello1/app/{name}")
    public String hello1(@PathVariable String name) {
        System.out.println("hello1=====");
        return "hello1 " + name;
    }

    @GetMapping("/hello2/app/{name}")
    @ResponseBody
    public User hello2(@PathVariable String name) {
        System.out.println("hello2=====");
        User user = new User();
        user.setUsername("hello2 " + name);
        return user;
    }

    @GetMapping("/hello3/app/{id:\\d+}")
    @ResponseBody
    public User hello3(@PathVariable String id) {
        System.out.println("hello3=====");
        User user = new User();
        user.setUsername("hello3 " + id);
        return user;
    }

    @PostMapping("/hello11/app/post")
    @ResponseBody
    public User hello11(@RequestBody User u) {
        System.out.println("hello11=====");
        u.setUsername("hello11 ");
        return u;
    }

    @PostMapping("/openfeign/provider/order2")
    public User getUser(@RequestBody User user){
        System.out.println("hello openfeign");
        user.setUsername("helloOpenFeign");
        return user;
    }
}
