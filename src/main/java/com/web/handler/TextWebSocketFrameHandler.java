package com.web.handler;

import com.util.Cache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: chenqimiao
 * Date: 2017/9/6
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel incoming = ctx.channel();
        String address = incoming.remoteAddress().toString();
        String message = msg.text();
        for (Channel channel : channels) {
                if(message.startsWith("sni")){

                    Cache.dictionary.put(address,msg.text().replace("sni",""));
                    channel.writeAndFlush(new TextWebSocketFrame(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "\n" +Cache.dictionary.get(address)+" " + "加入聊天室"));

                }else {

                    channel.writeAndFlush(new TextWebSocketFrame(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") + "\n" +Cache.dictionary.get(address)+": " + message));
                }

                //output current message to context.
                StringBuffer sb = new StringBuffer();
                sb.append(incoming.remoteAddress()).append("->").append(msg.text());
                System.out.println(sb.toString());
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
           // channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - "  + incoming.remoteAddress() + " 加入"));
        }
        channels.add(ctx.channel());
        //System.out.println("Client:" + Cache.dictionary.get(incoming.remoteAddress()) + "加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {

            channel.writeAndFlush(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") +" "+"Client:" + Cache.dictionary.get(incoming.remoteAddress().toString()) + "离开");
        }
        System.out.println(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") +" "+"Client:" + Cache.dictionary.get(incoming.remoteAddress().toString()) + "离开");
        channels.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
       // System.out.println("Client:" + Cache.dictionary.get(incoming.remoteAddress()) + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") +" "+"Client:" + Cache.dictionary.get(incoming.remoteAddress().toString()) + "掉线");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel incoming = ctx.channel();
        System.out.println(DateTime.now().toString("yyyy-MM-dd HH:mm:ss") +" "+"Client:" + Cache.dictionary.get(incoming.remoteAddress().toString()) + "异常");

        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}