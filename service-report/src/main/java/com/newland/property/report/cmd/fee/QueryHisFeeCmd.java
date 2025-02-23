package com.newland.property.report.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.intf.report.IReportCommunityInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询费用变更记录
 */
@NewlandPropertyCmd(serviceCode = "fee.queryHisFee")
public class QueryHisFeeCmd extends Cmd {

    @Autowired
    private IReportCommunityInnerServiceSMO reportCommunityInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson,"communityId","未包含小区");


    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        int row = reqJson.getInteger("row");
        FeeDto feeDto = BeanConvertUtil.covertBean(reqJson, FeeDto.class);

        int total = reportCommunityInnerServiceSMOImpl.queryHisFeeCount(feeDto);
//        int count = 0;
        List<FeeDto> feeDtos = null;
        if (total > 0) {
            feeDtos = reportCommunityInnerServiceSMOImpl.queryHisFees(feeDto);
        } else {
            feeDtos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, feeDtos);
        context.setResponseEntity(responseEntity);
    }
}
