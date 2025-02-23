package com.newland.property.common.cmd.corders;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.order.ICordersInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "corders.listUnitemLog")
public class ListUnitemLogCmd extends Cmd {

    @Autowired
    private ICordersInnerServiceSMO cordersInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "bId", "未包含业务ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Map info = new HashMap();
        info.put("bId", reqJson.getString("bId"));
        Map log = cordersInnerServiceSMOImpl.queryUnitemLog(info);

        if(log == null){
            throw new CmdException("数据不存在");
        }

        context.setResponseEntity(ResultVo.createResponseEntity(log));
    }
}
