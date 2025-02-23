package com.newland.property.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IMachineTranslateV1InnerServiceSMO;
import com.newland.property.po.machine.MachineTranslatePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@NewlandPropertyCmd(serviceCode = "machineTranslate.saveMachineTranslate")
public class SaveMachineTranslateCmd extends Cmd {

    @Autowired
    private IMachineTranslateV1InnerServiceSMO machineTranslateV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "machineCode", "必填，请填写设备编码");
        Assert.hasKeyAndValue(reqJson, "machineId", "必填，请填写设备版本号");
        Assert.hasKeyAndValue(reqJson, "typeCd", "必填，请选择对象类型");
        Assert.hasKeyAndValue(reqJson, "objName", "必填，请填写设备名称");
        Assert.hasKeyAndValue(reqJson, "objId", "必填，请填写对象Id");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请选择状态");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineTranslatePo machineTranslatePo = BeanConvertUtil.covertBean(reqJson, MachineTranslatePo.class);
        machineTranslatePo.setMachineTranslateId(GenerateCodeFactory.getGeneratorId("11"));
        int flag = machineTranslateV1InnerServiceSMOImpl.saveMachineTranslate(machineTranslatePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        context.setResponseEntity(ResultVo.success());
    }
}
