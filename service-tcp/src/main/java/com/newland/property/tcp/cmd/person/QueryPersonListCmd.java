package com.newland.property.tcp.cmd.person;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.tcp.server.TCPServerHandler;
import com.newland.property.utils.exception.CmdException;
import io.netty.channel.Channel;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "accessControl.queryPerions")
public class QueryPersonListCmd  extends Cmd {

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String deviceNo = reqJson.getString("deviceNo");
        int page = reqJson.getInteger("page");
        int row = reqJson.getInteger("row");
        if(TCPServerHandler.group.size() > 0){
            if(TCPServerHandler.group.iterator().hasNext()){
                Channel channel = TCPServerHandler.group.iterator().next();
                channel.writeAndFlush("");
            }
        }
    }
}
