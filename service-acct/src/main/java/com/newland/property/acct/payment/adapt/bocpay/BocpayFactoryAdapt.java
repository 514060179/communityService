package com.newland.property.acct.payment.adapt.bocpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.acct.payment.IPaymentFactoryAdapt;
import com.newland.property.acct.payment.adapt.bbgpay.lib.Base64Util;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.core.smo.IPaymentBusiness;
import com.newland.property.dto.payment.NotifyPaymentOrderDto;
import com.newland.property.dto.payment.PaymentOrderDto;
import com.newland.property.dto.paymentPoolValue.PaymentPoolValueDto;
import com.newland.property.dto.wechat.OnlinePayDto;
import com.newland.property.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.newland.property.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.wechat.OnlinePayPo;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.BocpayConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.*;
import com.newland.property.vo.api.bocpay.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 *
 */
@Service("bocpayFactoryAdapt")
public class BocpayFactoryAdapt implements IPaymentFactoryAdapt {

    private static final Logger logger = LoggerFactory.getLogger(BocpayFactoryAdapt.class);

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    /**
     *
     * @param paymentOrderDto
     * @param reqJson
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Map newlandPropertyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {
        String paymentPoolId = reqJson.getString("paymentPoolId");
        String communityId = reqJson.getString("communityId");
        String appId = context.getReqHeaders().get("app-id");
        String userId = context.getReqHeaders().get("user-id");
        String tradeType = reqJson.getString("tradeType");
        String feeId = reqJson.getString("feeId");
//        String notifyUrl =
//                MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
//                        BocpayConstant.BOCPAY_PAYMENT_NOTIFY_URL_HOST_URL) + "/app/payment/notify/bocpay/" + appId +
//                        "/" + communityId;
        String notifyUrl = "api/pay/666666/boc/callback";
        String business = reqJson.getString("business");

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setCommunityId(communityId);
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos =
                paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);
        Map<String, String> bocpayMap = new HashMap<>();
        for (PaymentPoolValueDto item : paymentPoolValueDtos) {
            bocpayMap.put(item.getColumnKey(), item.getColumnValue());
        }

        String merchantId = bocpayMap.get("merchantId");
        String terminalNo = bocpayMap.get("terminalNo");
        String privateKey = bocpayMap.get("privateKey");

        String amount = new BigDecimal(paymentOrderDto.getMoney()).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).toString();

        String orderDate = AllPayUtils.getDate();
        String orderTime = AllPayUtils.getTime();

        String orderId = paymentOrderDto.getOrderId();

        SortedMap<Object, Object> reqParameterMap = new TreeMap<Object, Object>();
        reqParameterMap.put("requestId", IdUtil.generateId("app"));
        reqParameterMap.put("version", "2.0");
        reqParameterMap.put("signType", "RSA2");
        reqParameterMap.put("merchantId", merchantId);
        reqParameterMap.put("terminalNo", terminalNo);
        reqParameterMap.put("amount", amount);
        reqParameterMap.put("originalAmount", amount);
        reqParameterMap.put("subject", MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
                BocpayConstant.BOCPAY_PAYMENT_ORDER_SUBJECT) + "-" + orderId);
        reqParameterMap.put("mercOrderNo", orderId);
        reqParameterMap.put("orderDate", orderDate);
        reqParameterMap.put("orderTime", orderTime);
        reqParameterMap.put("notifyUrl", notifyUrl);
        reqParameterMap.put("referUrl", MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN, BocpayConstant.BOCPAY_PAYMENT_REFER_URL));
        reqParameterMap.put("businessType", "5");
        reqParameterMap.put("otherBusinessType", business);

        if ("app".equals(tradeType)) {
            reqParameterMap.put("service", "CreateAASAppTrade");
        } else {
            reqParameterMap.put("service", "CreateBocCashier");
            reqParameterMap.put("groupUserId", userId);
            reqParameterMap.put("cashierLanguage", "zh_TW");
            reqParameterMap.put("payChannel", "ALL");
            reqParameterMap.put("productCode", "MOBILEWEB");
            reqParameterMap.put("pageUrl", MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN, BocpayConstant.BOCPAY_PAYMENT_PAGE_URL_HOST_URL) +
                    "pages/successPage/successPage?msg=%E6%94%AF%E4%BB%98%E6%88%90%E5%8A%9F");
        }

        String signStr = AllPayUtils.getUnSignStr("UTF-8", reqParameterMap);
        //获取公私钥
        String signed = SignUtil.signater(privateKey, signStr);
        logger.info("签名参数" + StringUtil.trim(net.sf.json.JSONObject.fromObject(reqParameterMap)));
        reqParameterMap.put("merchantSign", signed);

        String requestUrl = MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN, BocpayConstant.BOCPAY_PAYMENT_ENDPOINT);

        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(reqParameterMap);

        logger.info("请求地址：" + requestUrl);
        String requestStr = jsonObject.toString();
        requestStr = URLEncoder.encode(requestStr, BocpayConstant.MESSAGE_ENCODE);
        logger.info("所有请求参数：" + requestStr);

//        String result = URLDecoder.decode(HttpsUtils.bocAasrequest(requestUrl, "POST", requestStr, "application/json;" +
//                " charset=UTF-8"));

        String result = URLDecoder.decode(HttpsUtils.sendJsonPost(requestUrl, new HashMap<>(), requestStr));

        logger.info("返回参数：" + result);

        net.sf.json.JSONObject resultJSON = net.sf.json.JSONObject.fromObject(result);
        if ("000000".equals(resultJSON.getString("returnCode"))) {
            resultJSON.put("code", "200");
            resultJSON.put("msg", resultJSON.getString("returnMessage"));
        } else {
            resultJSON.put("code", "-200");
            resultJSON.put("msg", resultJSON.getString("returnMessage"));
        }

        if ("app".equals(tradeType)) {
            resultJSON.put("supportPaytype", MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN, BocpayConstant.BOCPAY_PAYMENT_SUPPORTPAYTYPE));

            resultJSON.put(BocpayConstant.BOCPAY_PAYMENT_IOS_BOCPAY_APP_ID,
                    MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
                            BocpayConstant.BOCPAY_PAYMENT_IOS_BOCPAY_APP_ID));
            resultJSON.put(BocpayConstant.BOCPAY_PAYMENT_ANDROID_BOCPAY_APP_ID,
                    MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
                            BocpayConstant.BOCPAY_PAYMENT_ANDROID_BOCPAY_APP_ID));
            resultJSON.put(BocpayConstant.BOCPAY_PAYMENT_WECHATPAY_APP_ID,
                    MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
                            BocpayConstant.BOCPAY_PAYMENT_WECHATPAY_APP_ID));
            resultJSON.put(BocpayConstant.BOCPAY_PAYMENT_WECHAT_PAY_LINK,
                    MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
                            BocpayConstant.BOCPAY_PAYMENT_WECHAT_PAY_LINK));
            resultJSON.put(BocpayConstant.BOCPAY_PAYMENT_ALIPAY_SCHEMES,
                    MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN,
                            BocpayConstant.BOCPAY_PAYMENT_ALIPAY_SCHEMES));
        }

        resultJSON.put("orderId", orderId);

        doSaveOnlinePay(appId, userId, orderId, business, paymentOrderDto.getMoney(), OnlinePayDto.STATE_WAIT, "待支付",
                paymentPoolId, feeId);

        return resultJSON;
    }

    /**
     * 支付回调
     * @param notifyPaymentOrderDto
     * @return
     */
    @Override
    public PaymentOrderDto newlandPropertyNotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto) {
        String param = notifyPaymentOrderDto.getParam();

        try {
            param = URLDecoder.decode(param, BocpayConstant.MESSAGE_ENCODE);

            BocChannelNotifyRequestBody responseBody =
                    (BocChannelNotifyRequestBody) net.sf.json.JSONObject.toBean(net.sf.json.JSONObject.fromObject(param)
                            , BocChannelNotifyRequestBody.class);
            logger.info("转换之后：" + responseBody.toString());
            String publicKey = "";
            if (!verifyResponseSignature(responseBody, publicKey)) {
                logger.info("验证签名失败");
                return null;
            }

            if ("SUCCESS".equals(responseBody.getStatus())) {
                logger.info("交易成功");
                String orderId = responseBody.getMerchantOrderNo();
                OnlinePayDto onlinePayDto = new OnlinePayDto();
                onlinePayDto.setOrderId(orderId);
                List<OnlinePayDto> onlinePays = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
                String feeId = onlinePays.get(0).getTransactionId();
                doUpdateOnlinePay(orderId, OnlinePayDto.STATE_COMPILE, "支付成功", feeId);
                PaymentOrderDto dto = new PaymentOrderDto();
                JSONObject result = new JSONObject();
                result.put("result", "SUCCESS");
                ResponseEntity<String> responseEntity = new ResponseEntity<String>(result.toJSONString(), HttpStatus.OK);
                dto.setResponseEntity(responseEntity);
                return dto;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param paymentOrderDto
     * @param reqJson
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Map queryPaymentStatus(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception {
        String paymentPoolId = reqJson.getString("paymentPoolId");
        String communityId = reqJson.getString("communityId");
        String feeId = reqJson.getString("feeId");

        String appId = context.getReqHeaders().get("app-id");

        String orderId = paymentOrderDto.getOrderId();

        Map<String, String> param = new HashMap<>();

        // 判断订单是否支付成功
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setOrderId(orderId);
        onlinePayDto.setState("C");
        List<OnlinePayDto> onlinePays = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if(onlinePays != null && !onlinePays.isEmpty()) {
            param.put("status", "1");
            return param;
        }

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setCommunityId(communityId);
        paymentPoolValueDto.setPpId(paymentPoolId);
        List<PaymentPoolValueDto> paymentPoolValueDtos =
                paymentPoolValueV1InnerServiceSMOImpl.queryPaymentPoolValues(paymentPoolValueDto);
        Map<String, String> bocpayMap = new HashMap<>();
        for (PaymentPoolValueDto item : paymentPoolValueDtos) {
            bocpayMap.put(item.getColumnKey(), item.getColumnValue());
        }

        String merchantId = bocpayMap.get("merchantId");
        String terminalNo = bocpayMap.get("terminalNo");
        String privateKey = bocpayMap.get("privateKey");
        String publicKey = bocpayMap.get("publicKey");

        BocChannelQueryRequestBody requestBody = new BocChannelQueryRequestBody(merchantId, terminalNo);
        requestBody.setIpAddress(IpUtil.getIpAddress());
        requestBody.setQueryNo(orderId);
        requestBody.setQueryLogNo("");

        param.put("status", "0");
        try {
            String response = URLDecoder.decode(request(requestBody, privateKey), BocpayConstant.MESSAGE_ENCODE);
            logger.error("调用中银订单查询结果：response={}", response);
            BocChannelQueryResponseBody bocChannelQueryResponseBody = JSON.parseObject(response, BocChannelQueryResponseBody.class);
            String signStr = RSAUtils.getSignStr(bocChannelQueryResponseBody);
            String signValue = bocChannelQueryResponseBody.getServerSign();
            if (!RSAUtils.verifyResponseSignature(signStr, signValue, publicKey)) {
                logger.error("参数验签失败signStr={},signValue={},publicKey={}", signStr, signValue, publicKey);
            }
            logger.info("调用中银订单查询结果：response={}", response);
            if ("000000".equals(bocChannelQueryResponseBody.getReturnCode())
                    && "S".equals(bocChannelQueryResponseBody.getResult())) {
                logger.info("交易成功");
                param = new HashMap<String, String>();
                param.put("status", "1");
                param.put("payOrdNo", bocChannelQueryResponseBody.getLogNo());
                param.put("orderId", orderId);
                param.put("paymentSchema", bocChannelQueryResponseBody.getPayChannel());
                param.put("name", bocChannelQueryResponseBody.getUserId());
                param.put("channelPayType", bocChannelQueryResponseBody.getPayChannel());

                doUpdateOnlinePay(orderId, OnlinePayDto.STATE_COMPILE, "支付成功", feeId);

                String paramIn = CommonCache.getAndRemoveValue("unifiedPayment_" + paymentOrderDto.getOrderId());

                if (StringUtil.isEmpty(paramIn)) {
                    throw new IllegalArgumentException("未找到业务数据");
                }

                JSONObject reqJsonCache = JSONObject.parseObject(paramIn);
                IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJsonCache.getString("business"), IPaymentBusiness.class);
                if (paymentBusiness == null) {
                    throw new CmdException("当前支付业务不支持");
                }
                paymentOrderDto.setAppId(appId);
                //2.0 相应业务 下单 返回 单号 ，金额，
                paymentBusiness.notifyPayment(paymentOrderDto, reqJsonCache);

                return param;
            }
        } catch (Exception e) {
            logger.error("中银查询支付异常", e);
        }

        return param;
    }

    private boolean verifyResponseSignature(Object obj, String publicKey)
            throws Exception {
        String signStr = getSignStr(obj);
        BocChannelRespone responeHead = (BocChannelRespone) obj;
        String signValue = responeHead.getServerSign();
        boolean flag = RSAUtils.verifySignatureWithSHA256(signStr, Base64Util.decode(signValue), publicKey);
        logger.info("返回验签结果：" + flag);
        if (!flag) {
            logger.info(obj.getClass().getName() + "\t响应签名字符串:" + signStr);
            logger.info("响应签名值：" + signValue);
        }
        return flag;
    }

    private String getSignStr(Object obj) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map map = BeanUtils.describe(obj);

        if (map.containsKey("defaultUI")) {
            Object defaultUI = map.get("defaultUI");
            map.remove("defaultUI");
            map.put("DefaultUI", defaultUI);
        }

        final TreeMap<String, Object> sortedParas = new TreeMap<String, Object>(map);
        String signStr = "";
        Iterator iterator = sortedParas.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if ("class".equalsIgnoreCase(key)) {
                continue;
            }

            if ("serverSign".equalsIgnoreCase(key)) {
                continue;
            }

            if ("merchantSign".equalsIgnoreCase(key)) {
                continue;
            }

            Object objVal = sortedParas.get(key);
            if (null == objVal) {
                continue;
            }

            String strVal = String.valueOf(objVal);
            if (StringUtils.isEmpty(strVal)) {
                continue;
            }
            signStr += strVal;
        }
        return signStr;
    }

    private String request(Object requestBody, String privateKey) throws Exception {

        String requestUrl = MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN, BocpayConstant.BOCPAY_PAYMENT_ENDPOINT);
        BocChannelRequest requestHead = (BocChannelRequest) requestBody;

        String requestId = IdUtil.generateId("RP");
        requestHead.setRequestId(requestId);

        String merchantSignStr = requestSign(requestBody, privateKey);
        requestHead.setMerchantSign(merchantSignStr);

        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(requestBody);
        if (jsonObject.containsKey("defaultUI")) {
            Object defaultUI = jsonObject.get("defaultUI");
            jsonObject.remove("defaultUI");
            jsonObject.put("DefaultUI", defaultUI);
        }
        logger.info("请求地址：" + requestUrl);
        String requestStr = jsonObject.toString();
        requestStr = URLEncoder.encode(requestStr, BocpayConstant.MESSAGE_ENCODE);
        logger.info("所有请求参数：" + requestStr);
        return HttpsUtils.bocAasrequest(requestUrl,  "post",requestStr,"");
    }

    private String requestSign(Object obj, String privateKey) throws Exception {
        String signStr = RSAUtils.getSignStr(obj);
        logger.info(obj.getClass().getName() + "\t请求待签名字符串:" + signStr);

        byte[] signByte = RSAUtils.signWithSHA256(signStr, privateKey);
        return Base64Util.encode(signByte);
    }

    private void doSaveOnlinePay(String appId, String openId, String orderId, String feeName,
                                 double money, String state, String message, String ppId, String feeId) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setAppId(appId);
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOpenId(openId);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setPayId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        onlinePayPo.setPayName(feeName);
        onlinePayPo.setRefundFee("0");
        onlinePayPo.setState(state);
        onlinePayPo.setTotalFee(money + "");
        onlinePayPo.setTransactionId(feeId);
        onlinePayPo.setPaymentPoolId(ppId);
        onlinePayV1InnerServiceSMOImpl.saveOnlinePay(onlinePayPo);
    }

    private void doUpdateOnlinePay(String orderId, String state, String message, String feeId) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);

//        if(OnlinePayDto.STATE_COMPILE.equals(state)) {
//            PayFeePo payFeePo = new PayFeePo();
//            payFeePo.setFeeId(feeId);
//            payFeePo.setState("2009001");
//            payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
//        }
    }
}
