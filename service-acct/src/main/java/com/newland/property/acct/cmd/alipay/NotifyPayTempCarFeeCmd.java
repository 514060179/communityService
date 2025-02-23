package com.newland.property.acct.cmd.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.CommunitySettingFactory;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.intf.community.IParkingAreaV1InnerServiceSMO;
import com.newland.property.intf.fee.ITempCarFeeCreateOrderV1InnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarOpenUserV1InnerServiceSMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.LinkedHashMap;

@NewlandPropertyCmd(serviceCode = "alipay.notifyPayTempCarFee")
public class NotifyPayTempCarFeeCmd extends Cmd {

    @Autowired
    private IOwnerCarOpenUserV1InnerServiceSMO ownerCarOpenUserV1InnerServiceSMOImpl;

    @Autowired
    private IParkingAreaV1InnerServiceSMO parkingAreaV1InnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private ITempCarFeeCreateOrderV1InnerServiceSMO tempCarFeeCreateOrderV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.jsonObjectHaveKey(reqJson, "out_trade_no", "请求报文中未包含订单信息");
        Assert.jsonObjectHaveKey(reqJson, "sign", "请求报文中未包含签名信息");

        String communityId = CommonCache.getAndRemoveValue(FeeDto.REDIS_PAY_TEMP_CAR_FEE_COMMUNITY + reqJson.getString("out_trade_no"));

        String resultInfo = reqJson.getString("resultInfo");
        String[] temp = resultInfo.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        //把拆分数据放在map集合内
        for (int i = 0; i < temp.length; i++) {
            String[] arr = temp[i].split("=", 2); //通过"="号分割成2个数据
            String[] tempAagin = new String[arr.length]; //再开辟一个数组用来接收分割后的数据
            for (int j = 0; j < arr.length; j++) {
                tempAagin[j] = arr[j];
            }
            map.put(tempAagin[0], tempAagin[1]);
        }
        System.out.println(map);
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map,
                    CommunitySettingFactory.getRemark(communityId, "ALIPAY_PUBLIC_KEY")
                    , "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
        if (!signVerified) {
            throw new CmdException("签名失败");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        ResponseEntity responseEntity = null;


        JSONObject paramIn = new JSONObject();
        paramIn.put("oId", reqJson.getString("out_trade_no"));
        responseEntity = tempCarFeeCreateOrderV1InnerServiceSMOImpl.notifyOrder(paramIn);
        context.setResponseEntity(responseEntity);

    }
}
