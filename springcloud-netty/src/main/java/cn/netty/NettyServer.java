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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * 没有网关时，ws://127.0.0.1:12345/v1/trainingSocket
 * 有网关时，ws://127.0.0.1:9999/ws2/v1/trainingSocket
 */
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
    }

    public void start() throws Exception {
        // boss线程监听端口，worker线程负责数据读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        UnorderedThreadPoolEventExecutor eventExecutorPool = new UnorderedThreadPoolEventExecutor(50, new DefaultThreadFactory("websocket"));
        try {
            ServerBootstrap sb = new ServerBootstrap(); // 辅助启动类
            sb.group(bossGroup, workGroup) // 绑定线程池
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG, 1024) // 链接缓冲池的大小（ServerSocketChannel的设置）
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作,从上往下
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            // Http协议数据编解码
                            pipeline.addLast(new HttpServerCodec());
                            // 以块的方式来写的处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            // Http协议数据片段聚合
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            pipeline.addLast(new OvertimeHandler());
                            pipeline.addLast(nettyServer.checkUserHandler);
                            pipeline.addLast(new WebSocketServerProtocolHandler("/v1/trainingSocket", null, true, 65536 * 10));
                            pipeline.addLast(nettyServer.metricHandler);
                            // handler一般都是放在最后面,建议一个就可以,如果业务复杂拆分多个,那在handler中调用fireXXX才会向下一个handler继续调用
                            pipeline.addLast(eventExecutorPool, nettyServer.myWebSocketHandler);
                        }
                    });
            // 绑定端口
            ChannelFuture cf = sb.bind(nettyServer.port).sync(); // 服务器异步创建绑定
            System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());

            // 等待服务端监听端口关闭
            // 对通道关闭进行监听，closeFuture是异步操作，监听通道关闭
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成
            cf.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }

    /**
     * 该启动方法有误，原因未知
     * @throws Exception
     */
    public void start2() throws Exception {
        // boss线程监听端口，worker线程负责数据读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        UnorderedThreadPoolEventExecutor eventExecutorPool = new UnorderedThreadPoolEventExecutor(50, new DefaultThreadFactory("websocket"));
        try {
            ServerBootstrap sb = new ServerBootstrap(); // 辅助启动类
            sb.group(bossGroup, workGroup) // 绑定线程池
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG, 1024) // 链接缓冲池的大小（ServerSocketChannel的设置）
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作,从上往下
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // LengthFieldPrepender编码器,将发送消息的前面加上请求体的字节长度
                            // LengthFieldBasedFrameDecoder获取请求头的长度,根据长度获取请求体的信息
                            // LengthFieldBasedFrameDecoder添加到pipeline的首位，因为其需要对接收到的数据
                            // 进行长度字段解码，这里也会对数据进行粘包和拆包处理
                            // maxFrameLength：指定了每个包所能传递的最大数据包大小；
                            // lengthFieldOffset：指定了长度字段在字节码中的偏移量；
                            // lengthFieldLength：指定了长度字段所占用的字节长度；
                            // lengthAdjustment：对一些不仅包含有消息头和消息体的数据进行消息头的长度的调整，这样就可以只得到消息体的数据，这里的lengthAdjustment指定的就是消息头的长度；
                            // initialBytesToStrip：对于长度字段在消息头中间的情况，可以通过initialBytesToStrip忽略掉消息头以及长度字段占用的字节。
                            // 1024最大数据包长度,包括长度所占的字节数
                            // 0,因为第一字符开始就是长度
                            // 2消息体长度所占字节数
                            // 0因为第一个字符就是长度
                            // 2去掉长度所占字节数,获取剩下的消息体
                            // 上面的2就是下面 LengthFieldPrepender 中的规定长度所占的字节数
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(4*10*1024, 0, 2, 0, 2));
                            // LengthFieldPrepender是一个编码器，主要是在响应字节数据前面添加字节长度字段
                            pipeline.addLast(new LengthFieldPrepender(2));
                            // StringDecoder要放到LengthFieldBasedFrameDecoder后面,对得到消息体的ByteBuf转码为String类型,个人认为这个比较常用
                            pipeline.addLast ("encode", new StringDecoder());

                            pipeline.addLast(new OvertimeHandler());
                            pipeline.addLast(nettyServer.checkUserHandler);
                            pipeline.addLast(new WebSocketServerProtocolHandler("/v1/trainingSocket", null, true, 65536 * 10));
                            pipeline.addLast(nettyServer.metricHandler);
                            // handler一般都是放在最后面,建议一个就可以,如果业务复杂拆分多个,那在handler中调用fireXXX才会向下一个handler继续调用
                            pipeline.addLast(eventExecutorPool, nettyServer.myWebSocketHandler);
                        }
                    });
            // 绑定端口
            ChannelFuture cf = sb.bind(nettyServer.port).sync(); // 服务器异步创建绑定
            System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());

//            cf.addListener (new ChannelFutureListener () {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    if (cf.isSuccess ()) {
//                        //这里只是简单的打印,
//                        System.out.println ("监听端口"+nettyServer.port+"成功");
//                    } else {
//                        System.out.println ("监听端口"+nettyServer.port+"失败");
//                    }
//                }
//            });

            // 等待服务端监听端口关闭
            // 对通道关闭进行监听，closeFuture是异步操作，监听通道关闭
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成
            cf.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
        }
    }
}
