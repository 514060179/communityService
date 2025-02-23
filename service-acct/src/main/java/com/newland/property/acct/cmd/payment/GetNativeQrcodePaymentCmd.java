package com.newland.property.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;

import java.text.ParseException;

/**
 * 查询native 支付信息
 */
@NewlandPropertyCmd(serviceCode = "payment.getNativeQrcodePayment")
public class GetNativeQrcodePaymentCmd extends Cmd {
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "qrToken", "未包含token信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String param = CommonCache.getAndRemoveValue("nativeQrcodePayment_" + reqJson.getString("qrToken"));


        if (StringUtil.isEmpty(param)) {
            throw new CmdException("支付码已经过期");
        }

        context.setResponseEntity(ResultVo.createResponseEntity(JSONObject.parseObject(param)));
    }
}
