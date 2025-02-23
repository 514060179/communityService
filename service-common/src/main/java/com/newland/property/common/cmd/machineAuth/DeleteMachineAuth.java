package com.newland.property.common.cmd.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.machine.MachineAuthDto;
import com.newland.property.intf.common.IMachineAuthInnerServiceSMO;
import com.newland.property.po.machine.MachineAuthPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "machineAuth.deleteMachineAuth")
public class DeleteMachineAuth extends Cmd {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "authId", "请求报文中未包含授权ID");
        MachineAuthDto machineAuthDto = new MachineAuthDto();
        machineAuthDto.setAuthId(reqJson.getString("authId"));
        List<MachineAuthDto> machineAuthDtos = machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto);
        Assert.listOnlyOne(machineAuthDtos, "查询员工门禁授权错误！");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineAuthPo machineAuthPo = BeanConvertUtil.covertBean(reqJson, MachineAuthPo.class);
        int flag = machineAuthInnerServiceSMOImpl.deleteMachineAuth(machineAuthPo);
        if (flag < 1) {
            throw new CmdException("删除员工门禁授权失败");
        }
        context.setResponseEntity(ResultVo.success());
    }
}
