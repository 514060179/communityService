package com.newland.property.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.fee.bmo.IQueryOweFee;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@NewlandPropertyCmd(serviceCode = "/feeApi/listFeeObj")
public class ListFeeObjCmd extends Cmd {

    @Autowired
    private IQueryOweFee queryOweFeeImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "feeId", "未包含费用信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(reqJson.getString("feeId"));
        feeDto.setCommunityId(reqJson.getString("communityId"));
        if (reqJson.containsKey("cycle") && !StringUtil.isEmpty(reqJson.getString("cycle"))) {
            feeDto.setCycle(reqJson.getString("cycle"));
        }
        if (reqJson.containsKey("custEndTime") && !StringUtil.isEmpty(reqJson.getString("custEndTime"))) {
            feeDto.setCustEndTime(reqJson.getString("custEndTime"));
        }

        if("105".equals(reqJson.getString("cycle"))){
            feeDto.setCustomStartTime(reqJson.getString("customStartTime"));
            feeDto.setCustomEndTime(reqJson.getString("customEndTime"));
        }

        ResponseEntity<String> result = queryOweFeeImpl.listFeeObj(feeDto);
        context.setResponseEntity(result);
    }
}
