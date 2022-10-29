package cn.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ChannelHandler.Sharable
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 数据分析的开关, 0=不发送，1=发送
    public static Integer ifAnalyse = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);

    private String chatServiceNamePrefix = "chatServiceName_";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive - 与客户端建立连接，通道开启！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String str = (String)ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        System.out.println("str2:" + str);
        System.out.println("channelInactive - 与客户端断开连接，通道关闭！");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof TextWebSocketFrame) {
                // 获取参数
                Map<String, Object> map = getParam(msg, ctx);
                ctx.channel().attr(AttributeKey.valueOf("userId")).set("123");
            }
        } catch (Exception e) {
            LOGGER.error("MyWebSocketHandler.channelRead error。", e);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext chc, TextWebSocketFrame textWebSocketFrame) throws Exception {
//        System.out.println("channelRead0");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("MyWebSocketHandler：websocket exception。", cause);
        ctx.close();
    }

    private Map<String, Object> getParam(Object msg, ChannelHandlerContext ctx) {
        Map<String, Object> map = new HashMap<>();
        try {
            //正常的TEXT消息类型
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
//            System.out.println("客户端收到服务器数据：" + frame.text());
            String text = frame.text();
            // 拿到数据后先手动释放
            frame.release();
            System.out.println("接收到数据：" + text);
        } catch (Exception e) {
            return null;
        }
        return map;
    }
}