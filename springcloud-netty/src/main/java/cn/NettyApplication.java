package cn;

import cn.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class NettyApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);

        try {
            new NettyServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
