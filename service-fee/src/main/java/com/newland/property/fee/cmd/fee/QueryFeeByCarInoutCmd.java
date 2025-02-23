package com.newland.property.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.utils.constant.FeeTypeConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.ApiMainFeeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "fee.queryFeeByCarInout")
public class QueryFeeByCarInoutCmd extends Cmd {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "inoutId", "请求中未包含inoutId信息");
        Assert.jsonObjectHaveKey(reqJson, "state", "请求中未包含state信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        FeeDto feeDtoParamIn = BeanConvertUtil.covertBean(reqJson, FeeDto.class);
        feeDtoParamIn.setPayerObjId(reqJson.getString("inoutId"));
        feeDtoParamIn.setState(reqJson.getString("state"));
        feeDtoParamIn.setFeeTypeCd(FeeTypeConstant.FEE_TYPE_TEMP_DOWN_PARKING_SPACE);
        feeDtoParamIn.setFeeFlag("2006012");

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDtoParamIn);
        ResponseEntity<String> responseEntity = null;
        if (feeDtos == null || feeDtos.size() == 0) {
            responseEntity = new ResponseEntity<String>("{}", HttpStatus.OK);
            context.setResponseEntity(responseEntity);
            return ;
        }

        FeeDto feeDto = feeDtos.get(0);

        ApiMainFeeVo apiFeeVo = BeanConvertUtil.covertBean(feeDto, ApiMainFeeVo.class);

        responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiFeeVo), HttpStatus.OK);


        context.setResponseEntity(responseEntity);
    }
}
