package com.newland.property.acct.payment.business.chargeCard;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.smo.IPaymentBusiness;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.charge.ChargeMonthCardDto;
import com.newland.property.dto.charge.ChargeMonthOrderDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.payment.PaymentOrderDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IChargeMonthCardV1InnerServiceSMO;
import com.newland.property.intf.common.IChargeMonthOrderV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.charge.ChargeMonthOrderPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@NewlandPropertyCmdDoc(title = "月卡充值",
        description = "手机端月卡充值",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/payment.unifiedPayment",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "payment.unifiedPayment.preStoreChargeCard",
        seq = 1
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "business", length = 64, remark = "支付场景， preStoreOnline"),
        @NewlandPropertyParamDoc(name = "payAdapt", length = 64, remark = "支付适配器，非必填"),
        @NewlandPropertyParamDoc(name = "tradeType", length = 64, remark = "支付类型 NATIVE JSAPI APP"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "tradeType", length = 30, remark = "支付类型 NATIVE JSAPI APP"),
        @NewlandPropertyParamDoc(name = "personTel", length = 30, remark = "手机号"),
        @NewlandPropertyParamDoc(name = "cardId", length = 30, remark = "月卡ID"),
        @NewlandPropertyParamDoc(name = "receivedAmount", length = 30, remark = "充值金额")

})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 100 成功不需要唤起支付窗口，直接支付成功，可能从账户等做了扣款，其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{       \"business\":\"preStoreChargeCard\",\n" +
                "         personName:\"张三\",\n" +
                "         personTel:\"18909711111\",\n" +
                "         cardId:\"18909711111\",\n" +
                "         receivedAmount:\"100\",\n" +
                "         communityId:\"123123\"" +
                " }",
        resBody = "{'code':0,'msg':'成功'}"
)

/**
 * 场地预约
 */
@Service("preStoreChargeCard")
public class PreStoreChargeCardBusiness implements IPaymentBusiness {


    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private IChargeMonthOrderV1InnerServiceSMO chargeMonthOrderV1InnerServiceSMOImpl;

    @Autowired
    private IChargeMonthCardV1InnerServiceSMO chargeMonthCardV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    /**
     * @param context
     * @param reqJson{ personName:"",
     *                 personTel:"",
     *                 appointmentTime:"",
     *                 payWay:"",
     *                 communityId:"",
     *                 spaces:[{spaceId:'123',openTimes:[{hours:1},{hours:2}]}]
     *                 }
     * @return
     */
    @Override
    public PaymentOrderDto unified(ICmdDataFlowContext context, JSONObject reqJson) {

        //Assert.hasKeyAndValue(reqJson, "spaceId", "请求报文中未包含spaceId");
        Assert.hasKeyAndValue(reqJson, "cardId", "请求报文中未包含cardId");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        ChargeMonthCardDto chargeMonthCardDto = new ChargeMonthCardDto();
        chargeMonthCardDto.setCardId(reqJson.getString("cardId"));
        chargeMonthCardDto.setCommunityId(reqJson.getString("communityId"));
        List<ChargeMonthCardDto> chargeMonthCardDtos = chargeMonthCardV1InnerServiceSMOImpl.queryChargeMonthCards(chargeMonthCardDto);

        Assert.listOnlyOne(chargeMonthCardDtos, "月卡不存在");

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(Double.parseDouble(chargeMonthCardDtos.get(0).getCardPrice()));
        paymentOrderDto.setName("购买充电月卡");

        reqJson.put("receivableAmount", paymentOrderDto.getMoney());
        reqJson.put("receivedAmount", paymentOrderDto.getMoney());
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {

        String cardId = reqJson.getString("cardId");
        String communityId = reqJson.getString("communityId");
        //查询 业主是否有账户
        ChargeMonthCardDto chargeMonthCardDto = new ChargeMonthCardDto();
        chargeMonthCardDto.setCardId(cardId);
        chargeMonthCardDto.setCommunityId(communityId);
        List<ChargeMonthCardDto> chargeMonthCardDtos = chargeMonthCardV1InnerServiceSMOImpl.queryChargeMonthCards(chargeMonthCardDto);

        ChargeMonthOrderDto chargeMonthOrderDto = new ChargeMonthOrderDto();
        chargeMonthOrderDto.setPersonTel(reqJson.getString("personTel"));
        chargeMonthOrderDto.setQueryTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        chargeMonthOrderDto.setCardId(chargeMonthCardDtos.get(0).getCardId());
        List<ChargeMonthOrderDto> chargeMonthOrderDtos = chargeMonthOrderV1InnerServiceSMOImpl.queryChargeMonthOrders(chargeMonthOrderDto);
        Date startTime = null;
        if (chargeMonthOrderDtos == null || chargeMonthOrderDtos.size() < 1) {
            startTime = DateUtil.getCurrentDate();
        } else {
            startTime = DateUtil.getDateFromStringA(chargeMonthOrderDtos.get(0).getEndTime());
        }

        String endDate = DateUtil.getAddMonthStringA(startTime, Integer.parseInt(chargeMonthCardDtos.get(0).getCardMonth()));

        ChargeMonthOrderPo chargeMonthOrderPo = BeanConvertUtil.covertBean(reqJson, ChargeMonthOrderPo.class);
        chargeMonthOrderPo.setOrderId(paymentOrderDto.getOrderId());
        chargeMonthOrderPo.setStartTime(DateUtil.getFormatTimeString(startTime, DateUtil.DATE_FORMATE_STRING_A));
        chargeMonthOrderPo.setEndTime(endDate);
        chargeMonthOrderPo.setPrimeRate("5");
        chargeMonthOrderPo.setCardId(chargeMonthCardDtos.get(0).getCardId());
        chargeMonthOrderPo.setReceivableAmount(chargeMonthCardDtos.get(0).getCardPrice());
        chargeMonthOrderPo.setReceivedAmount(chargeMonthCardDtos.get(0).getCardPrice());

        chargeMonthOrderPo.setPersonName(getPersonName(reqJson));
        int flag = chargeMonthOrderV1InnerServiceSMOImpl.saveChargeMonthOrder(chargeMonthOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
    }


    private String getPersonName(JSONObject reqJson) {

        // todo 业主用 手机号查询
        OwnerDto tmpOwnerDto = new OwnerDto();
        tmpOwnerDto.setLink(reqJson.getString("personTel"));
        tmpOwnerDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(tmpOwnerDto);
        if (ownerDtos != null && ownerDtos.size() > 0) {
            return ownerDtos.get(0).getName();
        }

        //todo 非业主是游客
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("personTel"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (userDtos != null && userDtos.size() > 0) {
            return userDtos.get(0).getName();
        }
        throw new CmdException("业主不存在");
    }
}
