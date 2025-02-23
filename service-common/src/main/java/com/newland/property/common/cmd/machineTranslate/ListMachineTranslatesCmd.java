package com.newland.property.common.cmd.machineTranslate;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.machine.MachineTranslateDto;
import com.newland.property.intf.common.IMachineTranslateInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.machineTranslate.ApiMachineTranslateDataVo;
import com.newland.property.vo.api.machineTranslate.ApiMachineTranslateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "machineTranslate.listMachineTranslates")
public class ListMachineTranslatesCmd extends Cmd {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.jsonObjectHaveKey(reqJson,"communityId","请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        MachineTranslateDto machineTranslateDto = BeanConvertUtil.covertBean(reqJson, MachineTranslateDto.class);

        int count = machineTranslateInnerServiceSMOImpl.queryMachineTranslatesCount(machineTranslateDto);

        List<ApiMachineTranslateDataVo> machineTranslates = null;

        if (count > 0) {
            machineTranslates = BeanConvertUtil.covertBeanList(machineTranslateInnerServiceSMOImpl.queryMachineTranslates(machineTranslateDto), ApiMachineTranslateDataVo.class);
        } else {
            machineTranslates = new ArrayList<>();
        }

        ApiMachineTranslateVo apiMachineTranslateVo = new ApiMachineTranslateVo();

        apiMachineTranslateVo.setTotal(count);
        apiMachineTranslateVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineTranslateVo.setMachineTranslates(machineTranslates);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineTranslateVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
