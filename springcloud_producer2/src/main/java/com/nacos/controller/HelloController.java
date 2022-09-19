package com.nacos.controller;

import com.nacos.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/")
public class HelloController {

    // http://localhost:10011/hello1/app/1
    @GetMapping("/hello1/app/{name}")
    public String hello1(@PathVariable String name) {
        System.out.println("hello1===== 2");
        return "hello1 " + name + " 2";
    }

    @GetMapping("/hello2/app/{name}")
    @ResponseBody
    public User hello2(@PathVariable String name) {
        System.out.println("hello2===== 2");
        User user = new User();
        user.setUsername("hello2 " + name + " 2");
        return user;
    }

    @GetMapping("/hello3/app/{id:\\d+}")
    @ResponseBody
    public User hello3(@PathVariable String id) {
        System.out.println("hello3===== 2");
        User user = new User();
        user.setUsername("hello3 " + id + " 2");
        return user;
    }

    @PostMapping("/hello11/app/post")
    @ResponseBody
    public User hello11(@RequestBody User u) {
        System.out.println("hello11===== 2");
        u.setUsername("hello11 " + " 2");
        return u;
    }

    @PostMapping("/openfeign/provider/order2")
    public User getUser(@RequestBody User user){
        System.out.println("hello openfeign 2");
        user.setUsername("helloOpenFeign" + " 2");
        return user;
    }
}
