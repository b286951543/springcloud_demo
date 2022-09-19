package com.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class JaegerApplication {
    public static void main(String[] args) {
        SpringApplication.run(JaegerApplication.class, args);
    }
}
