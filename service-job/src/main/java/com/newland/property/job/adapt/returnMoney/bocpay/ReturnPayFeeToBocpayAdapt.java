package com.newland.property.job.adapt.returnMoney.bocpay;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.client.FtpUploadTemplate;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.file.FileDto;
import com.newland.property.dto.onlinePayRefund.OnlinePayRefundDto;
import com.newland.property.dto.system.Business;
import com.newland.property.dto.wechat.OnlinePayDto;
import com.newland.property.dto.wechat.SmallWeChatDto;
import com.newland.property.intf.acct.IOnlinePayRefundV1InnerServiceSMO;
import com.newland.property.intf.acct.IOnlinePayV1InnerServiceSMO;
import com.newland.property.intf.acct.IReturnMoneyV1InnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatV1InnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.po.onlinePayRefund.OnlinePayRefundPo;
import com.newland.property.po.wechat.OnlinePayPo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.OSSUtil;
import com.newland.property.utils.util.PayUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.*;


/**
 * 退费审核通过后 通知 中银智慧付平台退款处理
 *
 * @author moonny
 * @Date 2024-05-27 14:12
 */
@Component(value = "returnPayFeeToBocpayAdapt")
public class ReturnPayFeeToBocpayAdapt  extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeToBocpayAdapt.class);

    @Autowired
    private IOnlinePayV1InnerServiceSMO onlinePayV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private IOnlinePayRefundV1InnerServiceSMO onlinePayRefundV1InnerServiceSMOImpl;

    @Autowired
    private IReturnMoneyV1InnerServiceSMO returnMoneyV1InnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        OnlinePayPo onlinePayPo = BeanConvertUtil.covertBean(data, OnlinePayPo.class);
        try {
            doPayFeeMoney(onlinePayPo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知退款
     *
     * @param onlinePayPo
     */
    public void doPayFeeMoney(OnlinePayPo onlinePayPo) throws Exception {
        //查询小区信息
        OnlinePayDto onlinePayDto = new OnlinePayDto();
        onlinePayDto.setPayId(onlinePayPo.getPayId());
        onlinePayDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayDto> onlinePayDtos = onlinePayV1InnerServiceSMOImpl.queryOnlinePays(onlinePayDto);
        if (onlinePayDtos == null || onlinePayDtos.size() < 1) {
            return;
        }
        // todo 如果物业系统退款
        if (!StringUtil.isEmpty(onlinePayDtos.get(0).getPaymentPoolId())) {
            ResultVo resultVo = returnMoneyV1InnerServiceSMOImpl.returnMoney(onlinePayDtos.get(0));
            if (resultVo.getCode() == ResultVo.CODE_OK) {
                doUpdateOnlinePay(onlinePayDtos.get(0).getPayId(), OnlinePayDto.STATE_CT, "退款完成");
            } else {
                doUpdateOnlinePay(onlinePayDtos.get(0).getPayId(), OnlinePayDto.STATE_FT, resultVo.getMsg());
            }
            return;
        }

        //todo 商城还是走这里


        // todo 查询退费明细
//        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
//        onlinePayRefundDto.setPayId(onlinePayDtos.get(0).getPayId());
//        onlinePayRefundDto.setState(OnlinePayDto.STATE_WT);
//        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);
//        String tranNo = GenerateCodeFactory.getGeneratorId("11");
//        if (onlinePayRefundDtos != null && onlinePayRefundDtos.size() > 0) {
//            tranNo = onlinePayRefundDtos.get(0).getRefundId();
//        }
//
//
//        if ("SUCCESS".equals(resMap.get("return_code")) && "SUCCESS".equals(resMap.get("result_code"))) {
//            doUpdateOnlinePay(onlinePayDtos.get(0).getPayId(), OnlinePayDto.STATE_CT, "退款完成");
//        } else {
//            doUpdateOnlinePay(onlinePayDtos.get(0).getPayId(), OnlinePayDto.STATE_FT, resMap.get("return_msg"));
//        }
    }

    private void doUpdateOnlinePay(String payId, String state, String message) {
        OnlinePayPo onlinePayPo = new OnlinePayPo();
        onlinePayPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayPo.setPayId(payId);
        onlinePayPo.setState(state);
        onlinePayV1InnerServiceSMOImpl.updateOnlinePay(onlinePayPo);

        // todo 查询退费明细
        OnlinePayRefundDto onlinePayRefundDto = new OnlinePayRefundDto();
        onlinePayRefundDto.setPayId(payId);
        onlinePayRefundDto.setState(OnlinePayDto.STATE_WT);
        List<OnlinePayRefundDto> onlinePayRefundDtos = onlinePayRefundV1InnerServiceSMOImpl.queryOnlinePayRefunds(onlinePayRefundDto);

        if (onlinePayRefundDtos == null || onlinePayRefundDtos.size() < 1) {
            return;
        }

        OnlinePayRefundPo onlinePayRefundPo = new OnlinePayRefundPo();
        onlinePayRefundPo.setRefundId(onlinePayRefundDtos.get(0).getRefundId());
        onlinePayRefundPo.setMessage(message.length() > 1000 ? message.substring(0, 1000) : message);
        onlinePayRefundPo.setState(state);
        onlinePayRefundV1InnerServiceSMOImpl.updateOnlinePayRefund(onlinePayRefundPo);
    }
}
