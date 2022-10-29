package cn.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
public class OvertimeHandler extends IdleStateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OvertimeHandler.class);

    // 超过30秒自动关闭通道
    // todo 后面要改回来30
    private static final int time = 30;

    public OvertimeHandler() {
        super(time, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        LOGGER.info("关闭连接");
        // 参数传递
        // todo ctx.channel().attr(AttributeKey.valueOf("userId")).set("123");
        // todo ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        String str = (String)ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        System.out.println("str1:" + str);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("OvertimeHandler：websocket exception", cause);
        ctx.close();
    }
}
