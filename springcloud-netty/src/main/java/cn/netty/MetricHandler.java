package cn.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ChannelDuplexHandler 用于接收、下发数据
 * 既要处理入站事件又要处理出站事件，则可继承 ChannelDuplexHandler
 */
@Component
@ChannelHandler.Sharable
public class MetricHandler extends ChannelDuplexHandler {

    private AtomicLong totalConnectionNumber = new AtomicLong();

    private String chatServiceNamePrefix = "chatServiceName_";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 在线人数+1
        System.out.println("在线人数+1");
        totalConnectionNumber.incrementAndGet();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 在线人数-1
        System.out.println("在线人数-1");
        totalConnectionNumber.decrementAndGet();
        super.channelInactive(ctx);
    }
}
