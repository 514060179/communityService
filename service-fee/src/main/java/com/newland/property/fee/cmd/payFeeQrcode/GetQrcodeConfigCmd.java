package com.newland.property.fee.cmd.payFeeQrcode;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.payFeeQrcode.PayFeeQrcodeDto;
import com.newland.property.intf.fee.IPayFeeQrcodeV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/***
 * 查询二维码配置
 */
@NewlandPropertyCmd(serviceCode = "payFeeQrcode.getQrcodeConfig")
public class GetQrcodeConfigCmd extends Cmd {

    @Autowired
    private IPayFeeQrcodeV1InnerServiceSMO payFeeQrcodeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "pfqId", "未包含二维码信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        PayFeeQrcodeDto payFeeQrcodeDto = new PayFeeQrcodeDto();
        payFeeQrcodeDto.setCommunityId(reqJson.getString("communityId"));
        payFeeQrcodeDto.setPfqId(reqJson.getString("pfqId"));
        List<PayFeeQrcodeDto> payFeeQrcodeDtos = payFeeQrcodeV1InnerServiceSMOImpl.queryPayFeeQrcodes(payFeeQrcodeDto);

        Assert.listOnlyOne(payFeeQrcodeDtos, "二维码配置错误");

        context.setResponseEntity(ResultVo.createResponseEntity(payFeeQrcodeDtos.get(0)));
    }
}
