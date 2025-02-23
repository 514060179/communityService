package com.newland.property.acct.cmd.payment;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.acct.payment.IPaymentFactoryAdapt;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.core.smo.IPaymentBusiness;
import com.newland.property.dto.fee.PayFeeDto;
import com.newland.property.dto.payment.PaymentOrderDto;
import com.newland.property.doc.annotation.*;
import com.newland.property.dto.paymentPool.PaymentPoolDto;
import com.newland.property.dto.paymentPoolConfig.PaymentPoolConfigDto;
import com.newland.property.intf.acct.IPaymentPoolConfigV1InnerServiceSMO;
import com.newland.property.intf.acct.IPaymentPoolV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


@NewlandPropertyCmdDoc(title = "统一支付接口",
        description = "系统中的统一支付接口",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/payment.unifiedPayment",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "payment.unifiedPayment"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "business",length = 64, remark = "支付场景，比如场地预约 为 venueReservation"),
        @NewlandPropertyParamDoc(name = "payAdapt",length = 64, remark = "支付适配器，非必填"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "tradeType", length = 30, remark = "支付类型 NATIVE JSAPI APP"),
        @NewlandPropertyParamDoc(name = "...", length = 30, remark = "其他参数根据相应接口协议传"),

})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 100 成功不需要唤起支付窗口，直接支付成功，可能从账户等做了扣款，其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "....", type = "String", length = 250, remark = "相应支付厂商要求字段，具体参考 不同厂商协议"),

        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"business\":\"venueReservation\",\"communityId\":\"123123\",\"...\":\"...\"}",
        resBody="{\"code\":0,\"msg\":\"成功\",\"...\":\"...\"}"
)

/**
 * 统一支付接口
 */
@NewlandPropertyCmd(serviceCode = "payment.unifiedPayment")
public class UnifiedPaymentCmd extends Cmd{


    private static final Logger logger = LoggerFactory.getLogger(UnifiedPaymentCmd.class);

    protected static final String DEFAULT_PAYMENT_ADAPT = "wechatPaymentFactory";// 默认微信通用支付

    @Autowired
    private IPaymentPoolConfigV1InnerServiceSMO paymentPoolConfigV1InnerServiceSMOImpl;

    @Autowired
    private IPaymentPoolV1InnerServiceSMO paymentPoolV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    /**
     * 校验
     * @param event              事件对象
     * @param context 请求报文数据
     * @param reqJson
     * @throws CmdException
     */

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson,"business","未包含业务");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        logger.debug(">>>>>>>>>>>>>>>>支付参数报文,{}",reqJson.toJSONString());
        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");


        //1.0 查询当前支付的业务

        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJson.getString("business"), IPaymentBusiness.class);

        if(paymentBusiness == null){
            throw new CmdException("当前支付业务不支持");
        }

        //2.0 相应业务 下单 返回 单号 ，金额，
        PaymentOrderDto paymentOrderDto =  paymentBusiness.unified(context,reqJson);
        paymentOrderDto.setAppId(appId);
        paymentOrderDto.setUserId(userId);


        logger.debug(">>>>>>>>>>>>>>>>支付业务下单返回,{}",JSONObject.toJSONString(paymentOrderDto));

        // String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN,"HC_ENV");

        // 这里 演示环境不向微信下单
//        if ("DEV".equals(env) || "TEST".equals(env)) {
//            paymentBusiness.notifyPayment(paymentOrderDto,reqJson);
//            JSONObject param = new JSONObject();
//            param.put("code", "100");
//            param.put("msg", "演示环境不触发支付");
//            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
//            return ;
//        }

        // 3.0 如果支付金额为0 直接调用 支付完通知接口
        if (paymentOrderDto.getMoney() <= 0) {
            paymentBusiness.notifyPayment(paymentOrderDto, reqJson);
            JSONObject param = new JSONObject();
            param.put("code", "100");
            param.put("msg", "操作成功");
            context.setResponseEntity(new ResponseEntity(JSONObject.toJSONString(param), HttpStatus.OK));
            return;
        }

        // 3.0 寻找当前支付适配器
        String payAdapt = computeAdapt(reqJson.getString("business"), reqJson);
        //MappingCache.getValue(WechatConstant.WECHAT_DOMAIN, WechatConstant.PAYMENT_ADAPT);
        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

        payAdapt = StringUtil.isEmpty(payAdapt) ? DEFAULT_PAYMENT_ADAPT : payAdapt;

        if(reqJson.containsKey("payAdapt") && !StringUtil.isEmpty(reqJson.getString("payAdapt"))){
            payAdapt = reqJson.getString("payAdapt");
        }

        IPaymentFactoryAdapt tPayAdapt = ApplicationContextFactory.getBean(payAdapt, IPaymentFactoryAdapt.class);

        // 4.0 相应支付厂家下单
        Map result = null;
        try {
            result = tPayAdapt.newlandPropertyPayment(paymentOrderDto,reqJson, context);
        } catch (Exception e) {
            logger.error("支付异常",e);
            throw new CmdException(e.getLocalizedMessage());
        }
        ResponseEntity<String> responseEntity = new ResponseEntity(JSONObject.toJSONString(result), HttpStatus.OK);

        logger.debug("调用支付厂家返回,{}",responseEntity);
        context.setResponseEntity(responseEntity);

        // redis 中 保存 请求参数
        CommonCache.setValue("unifiedPayment_"+paymentOrderDto.getOrderId(),reqJson.toJSONString(),CommonCache.PAY_DEFAULT_EXPIRE_TIME);
    }

    /**
     * 计算适配器
     *
     * @param business
     * @param reqJson
     * @return
     */
    private String computeAdapt(String business, JSONObject reqJson) {

        String communityId = reqJson.getString("communityId");
        //todo 如果是单个费用缴费
        PaymentPoolDto paymentPoolDto = ifPayFeeBusiness(business, reqJson);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        //todo 如果是临时车
        paymentPoolDto = ifTempCarFeeBusiness(business, communityId);
        if (paymentPoolDto != null) {
            reqJson.put("paymentPoolId", paymentPoolDto.getPpId());
            return paymentPoolDto.getBeanJsapi();
        }

        //todo 按小区查询 支付信息
        paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_COMMUNITY);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            throw new IllegalArgumentException("小区未配置支付信息");
        }

        reqJson.put("paymentPoolId", paymentPoolDtos.get(0).getPpId());
        reqJson.put("paymentPool", paymentPoolDtos.get(0));
        return paymentPoolDtos.get(0).getBeanJsapi();
    }

    /**
     * 临时车场景处理
     *
     * @param business
     * @param communityId
     * @return
     */
    private PaymentPoolDto ifTempCarFeeBusiness(String business, String communityId) {
        if (!"tempCarFee".equals(business)) {
            return null;
        }
        //todo 按小区查询 支付信息
        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setCommunityId(communityId);
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_TEMP_CAT);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }

    private PaymentPoolDto ifPayFeeBusiness(String business, JSONObject reqJson) {
        String feeId = "";
        if (!"payFee".equals(business) || !reqJson.containsKey("feeId")) {
            return null;
        }

        feeId = reqJson.getString("feeId");
        if (StringUtil.isNumber(feeId)) {
            return null;
        }

        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setFeeId(feeId);
        feeDto.setCommunityId(reqJson.getString("communityId"));
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        if (feeDtos == null || feeDtos.isEmpty()) {
            return null;
        }

        PaymentPoolConfigDto paymentPoolConfigDto = new PaymentPoolConfigDto();
        paymentPoolConfigDto.setConfigId(feeDtos.get(0).getConfigId());
        paymentPoolConfigDto.setCommunityId(feeDtos.get(0).getCommunityId());
        List<PaymentPoolConfigDto> paymentPoolConfigDtos = paymentPoolConfigV1InnerServiceSMOImpl.queryPaymentPoolConfigs(paymentPoolConfigDto);
        if (paymentPoolConfigDtos == null || paymentPoolConfigDtos.isEmpty()) {
            return null;
        }

        PaymentPoolDto paymentPoolDto = new PaymentPoolDto();
        paymentPoolDto.setPpId(paymentPoolConfigDtos.get(0).getPpId());
        paymentPoolDto.setCommunityId(paymentPoolConfigDtos.get(0).getCommunityId());
        paymentPoolDto.setPayType(PaymentPoolDto.PAY_TYPE_FEE_CONFIG);
        paymentPoolDto.setState("Y");
        List<PaymentPoolDto> paymentPoolDtos = paymentPoolV1InnerServiceSMOImpl.queryPaymentPools(paymentPoolDto);
        if (paymentPoolDtos == null || paymentPoolDtos.isEmpty()) {
            return null;
        }

        return paymentPoolDtos.get(0);
    }
}
