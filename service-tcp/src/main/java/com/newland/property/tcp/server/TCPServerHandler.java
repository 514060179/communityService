package com.newland.property.tcp.server;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.log.LoggerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;

import java.util.Random;


/**
 * @author Moonny
 */
public class TCPServerHandler extends ChannelInboundHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TCPServerHandler.class);

    public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 获取到当前与服务器连接成功的channel
        Channel channel = ctx.channel();
        group.add(channel);
        System.out.println(channel.remoteAddress() + " 上线," + "在线数量：" + group.size());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", "0.13");
        jsonObject.put("cmd", "browse persons");
        jsonObject.put("cmd_number", new Random().nextInt(10000));
        jsonObject.put("page_no", 1);
        jsonObject.put("page_size", 5);

        String requestMessage = this.getMessage(jsonObject);
        ctx.writeAndFlush(requestMessage);

//        ctx.channel().writeAndFlush(requestMessage);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 获取到当前要断开连接的Channel
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线，" + "在线数量：" + group.size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("收到客户端消息:");
        System.out.println(msg);

        String msgStr = "";
        if(msg.toString().contains("version")){
            msgStr = "{\"version"+  msg.toString().split("version")[1];
        }else {
            msgStr = "{\"response" + msg.toString().split("response")[1];
        }

        JSONObject parse = JSONObject.parseObject(msgStr);
        System.out.println("parse = " + parse);

        String deviceNo = parse.getString("device_no");
        String cmd = parse.getString("cmd");

        System.out.println("device no: " + deviceNo + ", cmd: " + cmd);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("version", parse.getString("version"));
//        jsonObject.put("cmd", "browse persons");
//        jsonObject.put("cmd_number", new Random().nextInt(10000));
//        jsonObject.put("page_no", 1);
//        jsonObject.put("page_size", 5);
//
//        if("heart beat".equals(cmd)) {
//            String requestMessage = this.getMessage(jsonObject);
//            ctx.write(requestMessage);
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("TCPServer出现异常", cause);
        ctx.close();
    }

    private String getMessage(JSONObject obj) {
        String objStr = JSONObject.toJSONString(obj);

        StringBuilder request = new StringBuilder();
        request.append("HTTP/1.1 200 OK").append("\r\n");
        request.append("Content_Length:").append(objStr.length()).append("\r\n");
        request.append("Content-Type:application/json").append("\r\n");
        request.append("\r\n");
        request.append(objStr);

        System.out.println("request string: "+ request.toString());

        return request.toString();
    }
}