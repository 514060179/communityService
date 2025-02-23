package com.newland.property.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.common.IMachineTranslateV1InnerServiceSMO;
import com.newland.property.po.machine.MachineTranslatePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "machineTranslate.deleteMachineTranslate")
public class DeleteMachineTranslateCmd extends Cmd {

    @Autowired
    private IMachineTranslateV1InnerServiceSMO machineTranslateV1InnerServiceSMOImpl;
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "machineTranslateId", "同步ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(reqJson, MachineTranslatePo.class);

        int flag = machineTranslateV1InnerServiceSMOImpl.deleteMachineTranslate(machineTranslatePo);

        if(flag <1){
            throw new CmdException("删除失败");
        }
    }
}
