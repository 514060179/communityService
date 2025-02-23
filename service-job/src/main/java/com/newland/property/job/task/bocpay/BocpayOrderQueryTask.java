package com.newland.property.job.task.bocpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.payment.PaymentOrderDto;
import com.newland.property.dto.paymentPoolValue.PaymentPoolValueDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.dto.wechat.OnlinePayDto;
import com.newland.property.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.newland.property.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.newland.property.core.smo.IPaymentBusiness;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.wechat.OnlinePayPo;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.BocpayConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.*;
import com.newland.property.vo.api.bocpay.BocChannelQueryRequestBody;
import com.newland.property.vo.api.bocpay.BocChannelQueryResponseBody;
import com.newland.property.vo.api.bocpay.BocChannelRequest;
import com.newland.property.vo.api.bocpay.BocChannelRespone;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @program: NewlandProperty
 * @description: 定时任务 轮训查询中银支付订单
 * @author: Moonny
 * @create: 2024-05-27 15:32
 **/
@Component
public class BocpayOrderQueryTask extends TaskSystemQuartz {

    private static Logger logger = LoggerFactory.getLogger(BocpayOrderQueryTask.class);

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {
        // todo: 查询15分钟内没有完成支付的订单列表
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setState("W");
        onlinePayDto.setRow(10);
        onlinePayDto.setPage(1);
        List<OnlinePayDto> onlinePays = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if(onlinePays == null && onlinePays.isEmpty()) {
            return;
        }
        for(OnlinePayDto onlinepayItem : onlinePays) {
            if(System.currentTimeMillis() - 1000L * 60L * 15 < onlinepayItem.getCreateTime().getTime()) {
                String paymentPoolId = onlinepayItem.getPaymentPoolId();
                String orderId = onlinepayItem.getOrderId();

                PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
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
                    if ("000000".equals(bocChannelQueryResponseBody.getReturnCode()) && "S".equals(bocChannelQueryResponseBody.getResult())) {

                        logger.info("交易成功");

                        doUpdateOnlinePay(orderId, OnlinePayDto.STATE_COMPILE, "支付成功", onlinepayItem.getTransactionId());

                        String paramIn = CommonCache.getAndRemoveValue("unifiedPayment_" + orderId);

                        if (StringUtil.isEmpty(paramIn)) {
                            logger.error("未找到业务数据");
                            throw new IllegalArgumentException("未找到业务数据");
                        }

                        JSONObject reqJsonCache = JSONObject.parseObject(paramIn);
                        IPaymentBusiness paymentBusiness = ApplicationContextFactory.getBean(reqJsonCache.getString("business"), IPaymentBusiness.class);
                        if (paymentBusiness == null) {
                            throw new CmdException("当前支付业务不支持");
                        }
                        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
                        paymentOrderDto.setAppId(reqJsonCache.getString("appId"));
                        //2.0 相应业务 下单 返回 单号 ，金额，
                        paymentOrderDto.setOrderId(onlinepayItem.getOrderId());
                        paymentOrderDto.setUserId(onlinepayItem.getOpenId());
                        paymentBusiness.notifyPayment(paymentOrderDto, reqJsonCache);
                    }
                } catch (Exception e) {
                    logger.error("中银查询支付异常", e);
                }
            }
        }
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

    private void doUpdateOnlinePay(String orderId, String state, String message, String feeId) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setOrderId(orderId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);

        if(OnlinePayDto.STATE_COMPILE.equals(state)) {
            PayFeePo payFeePo = new PayFeePo();
            payFeePo.setFeeId(feeId);
            payFeePo.setState("2009001");
            payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);
        }
    }
}
