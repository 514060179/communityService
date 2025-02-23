package com.newland.property.acct.payment.business.venue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.smo.IPaymentBusiness;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunitySpaceDto;
import com.newland.property.dto.community.CommunitySpacePersonDto;
import com.newland.property.dto.community.CommunitySpacePersonTimeDto;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.payment.PaymentOrderDto;
import com.newland.property.intf.community.ICommunitySpacePersonTimeV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunitySpacePersonV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunitySpaceV1InnerServiceSMO;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.intf.user.IUserNotificationInnerServiceSMO;
import com.newland.property.po.communitySpace.CommunitySpacePersonPo;
import com.newland.property.po.communitySpace.CommunitySpacePersonTimePo;
import com.newland.property.doc.annotation.*;
import com.newland.property.po.user.UserNotificationPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@NewlandPropertyCmdDoc(title = "场地预约",
        description = "场地预约手机端发起支付",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/payment.unifiedPayment",
        resource = "acctDoc",
        author = "吴学文",
        serviceCode = "payment.unifiedPayment.venueReservation"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "business", length = 64, remark = "支付场景，比如场地预约 为 venueReservation"),
        @NewlandPropertyParamDoc(name = "payAdapt", length = 64, remark = "支付适配器，非必填"),
        @NewlandPropertyParamDoc(name = "tradeType", length = 64, remark = "支付类型 NATIVE JSAPI APP"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "tradeType", length = 30, remark = "支付类型 NATIVE JSAPI APP"),
        @NewlandPropertyParamDoc(name = "personName", length = 30, remark = "预约人"),
        @NewlandPropertyParamDoc(name = "personTel", length = 30, remark = "预约电话"),
        @NewlandPropertyParamDoc(name = "appointmentTime", length = 30, remark = "预约时间 YYYY-MM-DD"),
        @NewlandPropertyParamDoc(name = "payWay", length = 30, remark = "支付方式"),
        @NewlandPropertyParamDoc(name = "spaces", type = "Array", length = 0, remark = "场地"),
        @NewlandPropertyParamDoc(parentNodeName = "spaces", name = "spaceId", length = 30, remark = "场地"),
        @NewlandPropertyParamDoc(parentNodeName = "spaces", name = "openTimes", type = "Array", length = 0, remark = "预约时间"),
        @NewlandPropertyParamDoc(parentNodeName = "openTimes", name = "hours", length = 10, remark = "预约小时"),

})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 100 成功不需要唤起支付窗口，直接支付成功，可能从账户等做了扣款，其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{       \"business\":\"venueReservation\",\"communityId\":\"123123\",\n" +
                "         personName:\"张三\",\n" +
                "         personTel:\"18909711111\",\n" +
                "         appointmentTime:\"2022-12-12\",\n" +
                "         payWay:\"2\",\n" +
                "         communityId:\"123123\",          spaces:[{spaceId:'123',openTimes:[{hours:1},{hours:2}]}]\n" +
                " }",
        resBody = "{'code':0,'msg':'成功'}"
)

/**
 * 场地预约
 */
@Service("venueReservation")
public class VenueReservationPaymentBusiness implements IPaymentBusiness {

    @Autowired
    private ICommunitySpaceV1InnerServiceSMO communitySpaceV1InnerServiceSMOImpl;

    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private ICommunitySpacePersonV1InnerServiceSMO communitySpacePersonV1InnerServiceSMOImpl;

    @Autowired
    private ICommunitySpacePersonTimeV1InnerServiceSMO communitySpacePersonTimeV1InnerServiceSMOImpl;

    @Autowired
    private IUserNotificationInnerServiceSMO userNotificationInnerServiceSMOImpl;

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
        Assert.hasKeyAndValue(reqJson, "personName", "请求报文中未包含personName");
        Assert.hasKeyAndValue(reqJson, "personTel", "请求报文中未包含personTel");
        Assert.hasKeyAndValue(reqJson, "appointmentTime", "请求报文中未包含appointmentTime");
        Assert.hasKeyAndValue(reqJson, "payWay", "请求报文中未包含payWay");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

        if (!reqJson.containsKey("spaces")) {
            throw new IllegalArgumentException("未包含 场地");
        }

        JSONArray spaces = reqJson.getJSONArray("spaces");

        JSONArray openTimes = null;
        BigDecimal money = new BigDecimal(0);
        CommunitySpacePersonTimeDto communitySpaceOpenTimeDto = null;
        List<CommunitySpacePersonTimeDto> communitySpacePersonTimeDtos = null;
        for (int spaceIndex = 0; spaceIndex < spaces.size(); spaceIndex++) {
            openTimes = spaces.getJSONObject(spaceIndex).getJSONArray("openTimes");

            if (openTimes == null || openTimes.size() < 1) {
                throw new IllegalArgumentException("未包含 预约时间");
            }

            CommunitySpaceDto communitySpaceDto = new CommunitySpaceDto();
            communitySpaceDto.setSpaceId(spaces.getJSONObject(spaceIndex).getString("spaceId"));
            List<CommunitySpaceDto> communitySpaceDtos = communitySpaceV1InnerServiceSMOImpl.queryCommunitySpaces(communitySpaceDto);

            Assert.listOnlyOne(communitySpaceDtos, "场地不存在" + communitySpaceDto.getSpaceId());

            int openTime = 0;

            for (int timeIndex = 0; timeIndex < openTimes.size(); timeIndex++) {
                communitySpaceOpenTimeDto = new CommunitySpacePersonTimeDto();
                communitySpaceOpenTimeDto.setSpaceId(spaces.getJSONObject(spaceIndex).getString("spaceId"));
                communitySpaceOpenTimeDto.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
                communitySpaceOpenTimeDto.setAppointmentTime(reqJson.getString("appointmentTime"));
                communitySpaceOpenTimeDto.setStates(new String[]{"C", "W"});
                communitySpacePersonTimeDtos = communitySpacePersonTimeV1InnerServiceSMOImpl.queryCommunitySpacePersonTimes(communitySpaceOpenTimeDto);
                if (communitySpacePersonTimeDtos != null && communitySpacePersonTimeDtos.size() > 0) {
                    throw new IllegalArgumentException(openTimes.getJSONObject(timeIndex).getString("hours") + "已经被预约，不能重复预约");
                }
                openTime += 1;
            }
            money = money.add(new BigDecimal(openTime).multiply(new BigDecimal(communitySpaceDtos.get(0).getFeeMoney())).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        PaymentOrderDto paymentOrderDto = new PaymentOrderDto();
        paymentOrderDto.setOrderId(GenerateCodeFactory.getOId());
        paymentOrderDto.setMoney(money.doubleValue());
        paymentOrderDto.setName("预约费用");

        reqJson.put("receivableAmount", money.doubleValue());
        reqJson.put("receivedAmount", money.doubleValue());
        return paymentOrderDto;
    }

    @Override
    public void notifyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson) {
        JSONArray spaces = reqJson.getJSONArray("spaces");
        JSONObject spaceObj = null;
        for (int spaceIndex = 0; spaceIndex < spaces.size(); spaceIndex++) {
            spaceObj = spaces.getJSONObject(spaceIndex);
            CommunitySpacePersonPo communitySpacePersonPo = BeanConvertUtil.covertBean(reqJson, CommunitySpacePersonPo.class);
            communitySpacePersonPo.setSpaceId(spaceObj.getString("spaceId"));
            communitySpacePersonPo.setOrderId(paymentOrderDto.getOrderId());
            communitySpacePersonPo.setCspId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            if (StringUtil.isEmpty(communitySpacePersonPo.getState())) {
                communitySpacePersonPo.setState(CommunitySpacePersonDto.STATE_W);
            }
            int flag = communitySpacePersonV1InnerServiceSMOImpl.saveCommunitySpacePerson(communitySpacePersonPo);

            if (flag < 1) {
                throw new CmdException("保存数据失败");
            }

            if (!spaceObj.containsKey("openTimes")) {
                return;
            }

            JSONArray openTimes = spaceObj.getJSONArray("openTimes");

            if (openTimes == null || openTimes.size() < 1) {
                return;
            }
            CommunitySpacePersonTimePo communitySpacePersonTimePo = null;
            for (int timeIndex = 0; timeIndex < openTimes.size(); timeIndex++) {
                communitySpacePersonTimePo = new CommunitySpacePersonTimePo();
                communitySpacePersonTimePo.setCommunityId(communitySpacePersonPo.getCommunityId());
                communitySpacePersonTimePo.setCspId(communitySpacePersonPo.getCspId());
                communitySpacePersonTimePo.setHours(openTimes.getJSONObject(timeIndex).getString("hours"));
                communitySpacePersonTimePo.setSpaceId(communitySpacePersonPo.getSpaceId());
                communitySpacePersonTimePo.setTimeId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
                communitySpacePersonTimePo.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
                communitySpacePersonTimeV1InnerServiceSMOImpl.saveCommunitySpacePersonTime(communitySpacePersonTimePo);
            }

            // TODO: 预约场地成功发推送-jpush
            // appPage: /pages/appointment/myBoomList

            JPushMessageDto messageDto = new JPushMessageDto();
            messageDto.setCommunityId(communitySpacePersonTimePo.getCommunityId());
            messageDto.setUserId(paymentOrderDto.getUserId());
            messageDto.setPageUrl("/pages/appointment/myBoomList");
            messageDto.setTitle("场地预约");
            messageDto.setContent("您的场地预约成功");
            messageDto.setPageType("appPage");
            JPushMessageQueue.addMsg(messageDto);

            UserNotificationPo userNotificationPo = new UserNotificationPo();
            userNotificationPo.setCommunityId(communitySpacePersonTimePo.getCommunityId());
            userNotificationPo.setUserId(paymentOrderDto.getUserId());
            userNotificationPo.setNotificationId(0);
            userNotificationPo.setName("场地预约");
            userNotificationPo.setTitle("场地预约");
            userNotificationPo.setContent("您的场地预约成功");
            userNotificationPo.setPageType(0);
            userNotificationPo.setAppPageUrl("/pages/appointment/myBoomList");
            userNotificationPo.setH5PageUrl("");
            userNotificationPo.setBgImage("");
            userNotificationPo.setType(1);
            userNotificationInnerServiceSMOImpl.saveUserNotification(userNotificationPo);
        }
    }
}
