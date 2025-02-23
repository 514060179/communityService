package com.newland.property.acct.payment.adapt.bocpay;

import com.alibaba.fastjson.JSON;
import com.newland.property.acct.payment.IRefundMoneyAdapt;
import com.newland.property.acct.payment.adapt.bbgpay.lib.Base64Util;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.onlinePayRefund.OnlinePayRefundDto;
import com.newland.property.dto.paymentPool.PaymentPoolDto;
import com.newland.property.dto.paymentPoolValue.PaymentPoolValueDto;
import com.newland.property.dto.wechat.OnlinePayDto;
import com.newland.property.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.newland.property.intf.acct.IPaymentPoolValueV1InnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.BocpayConstant;
import com.newland.property.utils.httpUtil.HttpsUtils;
import com.newland.property.utils.util.IdUtil;
import com.newland.property.utils.util.RSAUtils;
import com.newland.property.vo.ResultVo;
import com.newland.property.vo.api.bocpay.BocChannelRefundRequestBody;
import com.newland.property.vo.api.bocpay.BocChannelRefundResponseBody;
import com.newland.property.vo.api.bocpay.BocChannelRequest;
import com.newland.property.vo.api.bocpay.BocChannelRespone;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Service("bocpayRefundMoney")
public class BocpayRefundMoneyAdapt implements IRefundMoneyAdapt {

    private static final Logger logger = LoggerFactory.getLogger(BocpayRefundMoneyAdapt.class);

    @Autowired
    private IPaymentPoolValueV1InnerServiceSMO paymentPoolValueV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Override
    public ResultVo refund(OnlinePayDto onlinePayDto, PaymentPoolDto paymentPoolDto) throws Exception {

        PaymentPoolValueDto paymentPoolValueDto = new PaymentPoolValueDto();
        paymentPoolValueDto.setCommunityId(paymentPoolDto.getCommunityId());
        paymentPoolValueDto.setPpId(paymentPoolDto.getPpId());
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

        BocChannelRefundRequestBody requestBody = new BocChannelRefundRequestBody(merchantId, terminalNo);

        String orderNo = onlinePayDto.getOrderId();

        // todo 查询退费明细
        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setPayId(onlinePayDto.getPayId());
        onlinePayRefundDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);
        String tranNo = GenerateCodeFactory.getGeneratorId("11");
        if (onlinePayRefundDtos != null && onlinePayRefundDtos.size() > 0) {
            tranNo = onlinePayRefundDtos.get(0).getRefundId();
        }

        requestBody.setPayOrderNo(onlinePayDto.getOrderId());
        requestBody.setRefundOrderNo(tranNo);
        requestBody.setRefundAmount(onlinePayDto.getRefundFee());
        BocChannelRefundResponseBody responseBody = null;

        String response = request(orderNo, "refund", requestBody, privateKey);
        logger.info("BocChannelRefundResponseBody-refund--" + response);
        if (null != response) {
            response = URLDecoder.decode(response, BocpayConstant.MESSAGE_ENCODE);
            responseBody = JSON.parseObject(response, BocChannelRefundResponseBody.class);
            boolean flag = verifyResponseSignature(responseBody, publicKey);
            if (!flag) {
                logger.info("第三方返回验签失败");
                return new ResultVo(ResultVo.CODE_ERROR, "退款失败：回验签失败");
            }
        }

        return new ResultVo(ResultVo.CODE_OK, "退款完成");
    }

    private String request(String orderNo, String requestType, Object requestBody, String privateKey) throws Exception {

        String requestUrl = MappingCache.getValue(BocpayConstant.BOCPAY_DOMAIN, BocpayConstant.BOCPAY_PAYMENT_ENDPOINT);

        BocChannelRequest requestHead = (BocChannelRequest) requestBody;

        String requestId = IdUtil.generateId("RP");
        requestHead.setRequestId(requestId);

        String merchantSignStr = requestSign(requestBody, privateKey);
        requestHead.setMerchantSign(merchantSignStr);

        JSONObject jsonObject = JSONObject.fromObject(requestBody);
        if (jsonObject.containsKey("defaultUI")) {
            Object defaultUI = jsonObject.get("defaultUI");
            jsonObject.remove("defaultUI");
            jsonObject.put("DefaultUI", defaultUI);
        }

        logger.info("请求地址：" + requestUrl);
        String requestStr = jsonObject.toString();
        requestStr = URLEncoder.encode(requestStr, BocpayConstant.MESSAGE_ENCODE);
        logger.info("所有请求参数：" + requestStr);

        return HttpsUtils.bocAasrequest(requestUrl, "POST", requestStr, "application/json; charset=UTF-8");
    }

    private String requestSign(Object obj, String privateKey) throws Exception {
        String signStr = getSignStr(obj);
        logger.info(obj.getClass().getName() + "\t请求待签名字符串:" + signStr);

        byte[] signByte = RSAUtils.signWithSHA256(signStr, privateKey);
        return Base64Util.encode(signByte);
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
}
