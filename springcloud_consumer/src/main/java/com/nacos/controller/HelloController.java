package com.nacos.controller;

import com.nacos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/discoverSvrInfo")
    @ResponseBody
    public URI getSvrUrl() throws Exception {
        // 如果是多个服务提供者
        ServiceInstance serviceInstance;
        if(true) {
            List<ServiceInstance> services = discoveryClient.getInstances("app-provider");
            serviceInstance = services.size() > 0 ? services.get(0) : null;
        }else {
            serviceInstance = loadBalancerClient.choose("app-provider");
        }

        // 发起调用
        // 1. 不同命名空间下 调用，会导致找不到实例
        if(serviceInstance == null) {
            throw new Exception("未获取到实例");
        }

        return serviceInstance.getUri();
    }

    // http://127.0.0.1:10002/hello/hello1/user/1234
    @RequestMapping("/hello1/user/{name}")
    public String hello1(@PathVariable String name) {
        // get 请求
        String forObject = restTemplate.getForObject("http://app-provider/hello1/app/" + name, String.class);
        return forObject;
    }

    // http://127.0.0.1:10002/hello/hello2/user/1234
    @RequestMapping("/hello2/user/{name}")
    @ResponseBody
    public User hello2(@PathVariable String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        // get 请求
        User u = restTemplate.getForEntity("http://app-provider/hello2/app/{name}", User.class, params).getBody();
        return u;
    }

    // http://127.0.0.1:10002/hello/hello3/user/1234
    @RequestMapping("/hello3/user/{id:\\d+}") // 只能接受数据类型的参数
    @ResponseBody
    public User hello3(@PathVariable String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", id);
        // 和 getForEntity 类似，可以站位，和接收map参数, 区别在于返回的类型是 getBody 的内容
        // get 请求
        User u = restTemplate.getForObject("http://app-provider/hello3/app/{name}", User.class, params);
        return u;
    }

    // http://127.0.0.1:10002/hello/hello11/user/1234
    @RequestMapping("/hello11/user/{id:\\d+}")
    @ResponseBody
    public String hello11(@PathVariable String id) {
        Map<String, String> map = new HashMap<>();
        map.put("username", "java rest api --" + id);
        // post 请求，主要有三种 postForObject  postForEntity  postForLocation
        String str = restTemplate.postForObject("http://app-provider/hello11/app/post", map, String.class);
        return str;
    }

    // http://127.0.0.1:10002/hello/hello12/user/1234
    @RequestMapping("/hello12/user/{id:\\d+}")
    @ResponseBody
    public User hello12(@PathVariable String id) {
        Map<String, String> map = new HashMap<>();
        map.put("username", "java rest api --" + id);
        // post 请求，主要有三种 postForObject  postForEntity  postForLocation
        User u = restTemplate.postForObject("http://app-provider/hello11/app/post", map, User.class);
        return u;
    }
}
