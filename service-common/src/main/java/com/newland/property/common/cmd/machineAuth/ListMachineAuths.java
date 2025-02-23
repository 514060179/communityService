package com.newland.property.common.cmd.machineAuth;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.machine.MachineAuthDto;
import com.newland.property.intf.common.IMachineAuthInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.machineAuth.ApiMachineAuthDataVo;
import com.newland.property.vo.api.machineAuth.ApiMachineAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "machineAuth.listMachineAuths")
public class ListMachineAuths extends Cmd {

    @Autowired
    private IMachineAuthInnerServiceSMO machineAuthInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineAuthDto machineAuthDto = BeanConvertUtil.covertBean(reqJson, MachineAuthDto.class);
        int count = machineAuthInnerServiceSMOImpl.queryMachineAuthsCount(machineAuthDto);
        List<ApiMachineAuthDataVo> machineAuths = null;
        if (count > 0) {
            machineAuths = BeanConvertUtil.covertBeanList(machineAuthInnerServiceSMOImpl.queryMachineAuths(machineAuthDto), ApiMachineAuthDataVo.class);
        } else {
            machineAuths = new ArrayList<>();
        }
        ApiMachineAuthVo apiMachineAuthVo = new ApiMachineAuthVo();
        apiMachineAuthVo.setTotal(count);
        apiMachineAuthVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineAuthVo.setMachineAuths(machineAuths);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineAuthVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
