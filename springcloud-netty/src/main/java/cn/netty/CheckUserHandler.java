package cn.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class CheckUserHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("进入CheckUserHandler-channelRead");
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            if (StringUtils.isNotEmpty(uri)) {

                request.setUri("/v1/trainingSocket");
                ctx.fireChannelRead(request.retain());
            } else {
                ctx.close();
                return;
            }
        } else if (msg instanceof CloseWebSocketFrame) {
//            System.out.println("close");
        }
        // 传递消息至下一个处理器
        ctx.fireChannelRead(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
//        System.out.println("进入CheckUserHandler-channelRead0");
    }
}
