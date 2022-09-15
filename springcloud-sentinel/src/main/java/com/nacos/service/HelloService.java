package com.nacos.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    // value 为资源名称，会显示在 Sentinel 控制台的链路里面
    // fallbackClass，blockHandlerClass，可用于指定回调的方法在哪个类，此时类里面的所有方法都需要为static
    @SentinelResource(value= "HelloService.hello()", fallback = "helloCallback", blockHandler = "helloCallback1")
    public String hello() {
        System.out.println("报错了===============");
//        String str = null;
//        str.length(); // 这里报错后，会自动执行 helloCallback 方法
        return "hello";
    }

    // 处理异常， 触发异常会自动执行(触发规则后，如果没有helloCallback1则会回调到该方法)
    public String helloCallback(Throwable throwable) {
        System.out.println("异常时回调========");
        return "这是异常回调 ---> " + throwable.getMessage();
    }

    // sentinel回退，触发规则会自动执行
    public String helloCallback1(BlockException e) {
        System.out.println("触发规则时回调========");
        return "sentinel 回退 ---> " + e.getMessage();
    }
}

