package com.newland.property.common.cmd.msg;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IMsgReadV1InnerServiceSMO;
import com.newland.property.po.message.MsgReadPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "msg.readMsg")
public class ReadMsgCmd extends Cmd {

    @Autowired
    private IMsgReadV1InnerServiceSMO msgReadV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "userName", "必填，请填写员工名称");
        Assert.hasKeyAndValue(reqJson, "userId", "必填，请填写员工ID");
        Assert.hasKeyAndValue(reqJson, "msgId", "必填，请填写消息ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        JSONObject businessMsgRead = new JSONObject();
        businessMsgRead.put("msgReadId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_msgReadId));
        businessMsgRead.putAll(reqJson);
        MsgReadPo msgReadPo = BeanConvertUtil.covertBean(businessMsgRead, MsgReadPo.class);

        int flag = msgReadV1InnerServiceSMOImpl.saveMsgRead(msgReadPo);

        if (flag < 1) {
            throw new CmdException("已读消息失败");
        }
    }
}
