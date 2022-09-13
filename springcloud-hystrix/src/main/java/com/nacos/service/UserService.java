package com.nacos.service;

import com.nacos.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    // 如果该接口报错或超时，则执行 getUserDefault 方法
    @HystrixCommand(
            // 服务降级配置，设置请求错误时的回调函数 getUserDefault ，还可以对降级的服务进行分组
            fallbackMethod = "getUserDefault", commandKey = "getUserById", groupKey = "userGroup", threadPoolKey = "getUserThread",
            // 服务熔断配置，具体看 HystrixCommandProperties 这个类
            commandProperties= {
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000"),
                    // 如果5秒内，请求达到了4个，错误率达到50%以上，就开启跳闸，
                    // 就会直接走fallback，业务代码将不再会调用
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 如果3秒以后再调用，会再执行一次业务方法
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "3000"),
                    // 信号隔离设置 - 在主线程执行
                    //只需要设置execution.isolation.strategy = SEMAPHORE即可
                    // 并设置并发数
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"),
                    @HystrixProperty(name="execution.isolation.semaphore.maxConcurrentRequests", value="10"),

                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="2000"), // 超时时间2s，默认1s
                    @HystrixProperty(name="execution.timeout.enabled", value="true") // 默认开启超时时间
            }

            // 以上几个参数的含义是：5秒种以内，请求次数达到4个以上且失败率达到50%以上，则开启跳闸。跳闸3秒以后如果有请求过来不会继续跳闸，
            // 而是去实际做请求，如果请求失败，继续保持跳闸的状态，如果请求成功，则取消跳闸。
    )
    public User hello(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
//        String a = null;
//        a.toString();
        User u = restTemplate.getForObject("http://app-provider/hello2/app/{name}", User.class, params);
        return u;
    }

    // 如果该方法报错或超时，则会执行 getUserDefault1。可以通过 ignoreExceptions 去忽略某些异常
    @HystrixCommand(fallbackMethod = "getUserDefault1")
//    @HystrixCommand(fallbackMethod = "getUserDefault1", ignoreExceptions = {NullPointerException.class})
    public User getUserDefault(String name, Throwable e) {
        // 获取异常信息
        System.out.println(e.getMessage());

        // 测试服务降级 - 该方法会错误，然后回调 getUserDefault1
//        String a = null;
//        a.toString();

        User user = new User();
        user.setUsername("defaultUser");
        return user;
    }

    public User getUserDefault1(String name) {
        User user = new User();
        user.setUsername("defaultUser 1");
        return user;
    }
}
