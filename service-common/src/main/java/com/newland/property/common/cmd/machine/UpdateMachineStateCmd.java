package com.newland.property.common.cmd.machine;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.machine.MachineDto;
import com.newland.property.intf.common.IMachineInnerServiceSMO;
import com.newland.property.intf.common.IMachineV1InnerServiceSMO;
import com.newland.property.po.machine.MachinePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "machine.updateMachineState")
public class UpdateMachineStateCmd extends Cmd {

    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IMachineV1InnerServiceSMO machineV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "machineId", "设备ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写设备状态");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(reqJson.getString("communityId"));
        machineDto.setMachineId(reqJson.getString("machineId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "根据设备编码查询到多条记录，请检查数据");
        JSONObject businessMachine = new JSONObject();
        businessMachine.putAll(BeanConvertUtil.beanCovertMap(machineDtos.get(0)));
        businessMachine.put("state", reqJson.getString("state"));
        //计算 应收金额
        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        int flag = machineV1InnerServiceSMOImpl.updateMachine(machinePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }
    }
}
