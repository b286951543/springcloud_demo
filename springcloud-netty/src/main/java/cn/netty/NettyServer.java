package cn.netty;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class NettyServer {
    @Value("${netty.port}")
    private int port;

    private static NettyServer nettyServer;

    @Autowired
    private MetricHandler metricHandler;

    @Autowired
    private MyWebSocketHandler myWebSocketHandler;

    @Autowired
    private CheckUserHandler checkUserHandler;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @PostConstruct
    public void init() {
        nettyServer = this;
        nettyServer.port = this.port;
        nettyServer.metricHandler = this.metricHandler;
        nettyServer.myWebSocketHandler = this.myWebSocketHandler;
        nettyServer.checkUserHandler = this.checkUserHandler;
        nettyServer.nacosServiceManager = this.nacosServiceManager;
        nettyServer.nacosDiscoveryProperties = this.nacosDiscoveryProperties;

        try {
            // 除了要注册 application 服务，还需要注册 netty 服务，否则会找不到
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            namingService.registerInstance("app-netty2", "g_nacos_01", "127.0.0.1", 12345);
        }catch (Exception e){

        }
//        try {
//            //获取nacos服务
//            NamingService namingService = NamingFactory.createNamingService("127.0.0.1:8848");
//            //将服务注册到注册中心
//            namingService.registerInstance("app-netty", "g_nacos_01", "127.0.0.1", Integer.valueOf("8848"));
//        } catch (NacosException e) {
//            System.out.println("注册失败");
//        }
//        System.out.println("成功--------");
    }

    public void start() throws Exception {
        // boss线程监听端口，worker线程负责数据读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        UnorderedThreadPoolEventExecutor eventExecutorPool = new UnorderedThreadPoolEventExecutor(50, new DefaultThreadFactory("websocket"));
        try {
            ServerBootstrap sb = new ServerBootstrap(); // 辅助启动类
            sb.group(bossGroup, workGroup) // 绑定线程池
                    .option(ChannelOption.SO_BACKLOG, 1024) // 链接缓冲池的大小（ServerSocketChannel的设置）
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作,从上往下
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块的方式来写的处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            pipeline.addLast(new OvertimeHandler());
                            pipeline.addLast(nettyServer.checkUserHandler);
                            pipeline.addLast(new WebSocketServerProtocolHandler("/v1/trainingSocket", null, true, 65536 * 10));
                            pipeline.addLast(nettyServer.metricHandler);
                            pipeline.addLast(eventExecutorPool, nettyServer.myWebSocketHandler);
                        }
                    });
            // 绑定端口
            ChannelFuture cf = sb.bind(nettyServer.port).sync(); // 服务器异步创建绑定
            System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            // 等待服务端监听端口关闭
            cf.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
}
