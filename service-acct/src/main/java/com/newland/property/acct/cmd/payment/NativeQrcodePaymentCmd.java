package com.newland.property.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.smo.IPaymentBusiness;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.payment.PaymentOrderDto;
import com.newland.property.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.newland.property.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.UrlCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

/**
 * native qrcode 支付
 * add by wuxw 2023-06-25
 */
@NewlandPropertyCmd(serviceCode = "payment.nativeQrcodePayment")
public class NativeQrcodePaymentCmd extends Cmd {


    private static final Logger logger = LoggerFactory.getLogger(NativeQrcodePaymentCmd.class);

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatNativeQrcodePaymentFactory";// 默认微信通用支付

    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "business", "未包含业务");
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}", reqJson.toJSONString());
        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        reqJson.put("createUserId", userId);
        reqJson.put("storeId", CmdContextUtils.getStoreId(context));

        //1.0 查询当前支付的业务

        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if (paymentBusiness == null) {
            throw new CmdException("当前支付业务不支持");
        }

        //2.0 相应业务 下单 返回 单号 ，金额，
        PaymentOrderDto paymentOrderDto = paymentBusiness.unified(context, reqJson);
        paymentOrderDto.setAppId(appId);
        paymentOrderDto.setUserId(userId);
        reqJson.put("money", paymentOrderDto.getMoney());

        String token = GenerateCodeFactory.getUUID();

        // redis 中 保存 请求参数
        CommonCache.setValue("nativeQrcodePayment_" + token, reqJson.toJSONString(), CommonCache.PAY_DEFAULT_EXPIRE_TIME);
        JSONObject result = new JSONObject();
        result.put("codeUrl", UrlCache.getOwnerUrl() + "/#/pages/fee/qrCodeCashier?qrToken=" + token);

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(result);

        logger.debug("调用支付厂家返回,{}", responseEntity);
        context.setResponseEntity(responseEntity);

    }

}
