package com.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Properties;
import java.util.concurrent.Executor;

@EnableDiscoveryClient
@SpringCloudApplication
public class NacosAppication {
    public static void main(String[] args) {
        SpringApplication.run(NacosAppication.class, args);
    }

//    public static void main(String[] args) throws NacosException {
//        // 使用nacos client 远程获取nacos服务上的配置信息
//
//        // nacos 地址
//        String serverAddr = "127.0.0.1:8848";
//        // Data Id
//        String dataId = "app-nacos-dev.yml";
//        // Group
//        String group = "g_nacos_01";
//        Properties properties = new Properties();
//        properties.put("serverAddr", serverAddr);
//        properties.put("serverAddr", serverAddr);
//        // 获取配置
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        String config = configService.getConfig(dataId, group, 5000);
//        System.out.println(config);
//    }

//    public static void main(String[] args) throws NacosException, InterruptedException {
//        String serverAddr = "localhost";
//        String dataId = "app-nacos.yml";
//        String group = "g_nacos_01";
//        String namespace = "30e9cb88-4054-4714-8dbc-d71c8e960e82";
//
//        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
//        properties.put(PropertyKeyConst.NAMESPACE, namespace);
//
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        String content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);
//        configService.addListener(dataId, group, new Listener() {
//            @Override
//            public void receiveConfigInfo(String configInfo) {
//                System.out.println("recieve:" + configInfo);
//            }
//
//            @Override
//            public Executor getExecutor() {
//                return null;
//            }
//        });
//
//        boolean isPublishOk = configService.publishConfig(dataId, group, "content");
//        System.out.println(isPublishOk);
//
//        Thread.sleep(3000);
//        content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);
//
//        boolean isRemoveOk = configService.removeConfig(dataId, group);
//        System.out.println(isRemoveOk);
//        Thread.sleep(3000);
//
//        content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);
//        Thread.sleep(300000);
//
//    }
}
