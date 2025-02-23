package com.newland.property.fee.cmd.feeConfig;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.intf.fee.IPayFeeConfigV1InnerServiceSMO;
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
 * 查询 费用对象
 */
@NewlandPropertyCmd(serviceCode = "feeConfig.listConfigFeeObjs")
public class ListConfigFeeObjsCmd extends Cmd {

    @Autowired
    private IPayFeeConfigV1InnerServiceSMO payFeeConfigV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "configId", "未包含费用项");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        FeeConfigDto feeConfigDto = BeanConvertUtil.covertBean(reqJson, FeeConfigDto.class);

        int count = payFeeConfigV1InnerServiceSMOImpl.queryFeeObjsCount(feeConfigDto);

        List<FeeDto> feeDtos = null;

        if (count > 0) {
            feeDtos = BeanConvertUtil.covertBeanList(payFeeConfigV1InnerServiceSMOImpl.queryFeeObjs(feeConfigDto), FeeDto.class);
        } else {
            feeDtos = new ArrayList<>();
        }


        ResponseEntity<String> responseEntity
                = ResultVo.createResponseEntity(
                (int) Math.ceil((double) count / (double) reqJson.getInteger("row")),
                count,
                feeDtos);

        context.setResponseEntity(responseEntity);
    }
}
