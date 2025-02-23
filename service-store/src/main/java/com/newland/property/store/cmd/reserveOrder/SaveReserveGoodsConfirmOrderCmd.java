/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.store.cmd.reserveOrder;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunitySpacePersonTimeDto;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.reserve.ReserveGoodsConfirmOrderDto;
import com.newland.property.dto.reserve.ReserveGoodsOrderDto;
import com.newland.property.dto.reserve.ReserveGoodsOrderTimeDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.store.IReserveGoodsConfirmOrderV1InnerServiceSMO;
import com.newland.property.intf.store.IReserveGoodsOrderTimeV1InnerServiceSMO;
import com.newland.property.intf.store.IReserveGoodsOrderV1InnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.intf.user.IUserNotificationInnerServiceSMO;
import com.newland.property.po.reserve.ReserveGoodsConfirmOrderPo;
import com.newland.property.po.reserve.ReserveGoodsOrderTimePo;
import com.newland.property.po.user.UserNotificationPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：reserveGoodsConfirmOrder.saveReserveGoodsConfirmOrder
 * 请求路劲：/app/reserveGoodsConfirmOrder.SaveReserveGoodsConfirmOrder
 * add by 吴学文 at 2022-12-06 16:15:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "reserveOrder.saveReserveGoodsConfirmOrder")
public class SaveReserveGoodsConfirmOrderCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveReserveGoodsConfirmOrderCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IReserveGoodsConfirmOrderV1InnerServiceSMO reserveGoodsConfirmOrderV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderTimeV1InnerServiceSMO reserveGoodsOrderTimeV1InnerServiceSMOImpl;

    @Autowired
    private IReserveGoodsOrderV1InnerServiceSMO reserveGoodsOrderV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserNotificationInnerServiceSMO userNotificationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "timeId", "请求报文中未包含timeId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReserveGoodsOrderTimeDto reserveGoodsOrderTimeDto = new ReserveGoodsOrderTimeDto();
        reserveGoodsOrderTimeDto.setTimeId(reqJson.getString("timeId"));
        reserveGoodsOrderTimeDto.setCommunityId(reqJson.getString("communityId"));
        reserveGoodsOrderTimeDto.setState(CommunitySpacePersonTimeDto.STATE_WAIT_CONFIRM);
        List<ReserveGoodsOrderTimeDto> reserveGoodsOrderTimeDtos = reserveGoodsOrderTimeV1InnerServiceSMOImpl.queryReserveGoodsOrderTimes(reserveGoodsOrderTimeDto);

        Assert.listOnlyOne(reserveGoodsOrderTimeDtos, "未包含预约记录");

        ReserveGoodsOrderDto reserveGoodsOrderDto = new ReserveGoodsOrderDto();
        reserveGoodsOrderDto.setOrderId(reserveGoodsOrderTimeDtos.get(0).getOrderId());
        List<ReserveGoodsOrderDto> reserveGoodsOrderDtos = reserveGoodsOrderV1InnerServiceSMOImpl.queryReserveGoodsOrders(reserveGoodsOrderDto);
        Assert.listOnlyOne(reserveGoodsOrderDtos, "预约订单不存在");

        //将 时间修改 核销中
        ReserveGoodsOrderTimePo reserveGoodsOrderTimePo = new ReserveGoodsOrderTimePo();
        reserveGoodsOrderTimePo.setTimeId(reserveGoodsOrderTimeDtos.get(0).getTimeId());
        reserveGoodsOrderTimePo.setState(CommunitySpacePersonTimeDto.STATE_FINISH);
        int flag = reserveGoodsOrderTimeV1InnerServiceSMOImpl.updateReserveGoodsOrderTime(reserveGoodsOrderTimePo);
        if (flag < 1) {
            throw new CmdException("核销预约失败");
        }

        ReserveGoodsConfirmOrderPo reserveGoodsConfirmOrderPo = BeanConvertUtil.covertBean(reqJson, ReserveGoodsConfirmOrderPo.class);
        reserveGoodsConfirmOrderPo.setCoId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        reserveGoodsConfirmOrderPo.setOrderId(reserveGoodsOrderTimeDtos.get(0).getOrderId());
        reserveGoodsConfirmOrderPo.setType(reserveGoodsOrderDtos.get(0).getType());
        reserveGoodsConfirmOrderPo.setGoodsId(reserveGoodsOrderDtos.get(0).getGoodsId());
        flag = reserveGoodsConfirmOrderV1InnerServiceSMOImpl.saveReserveGoodsConfirmOrder(reserveGoodsConfirmOrderPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        ReserveGoodsConfirmOrderDto reserveGoodsConfirmOrderDto = new ReserveGoodsConfirmOrderDto();
        reserveGoodsConfirmOrderDto.setCoId(reserveGoodsConfirmOrderPo.getCoId());
        List<ReserveGoodsConfirmOrderDto> reserveGoodsConfirmOrderDtos = reserveGoodsConfirmOrderV1InnerServiceSMOImpl.queryReserveGoodsConfirmOrders(reserveGoodsConfirmOrderDto);

        // todo 服务预约：核销成功通知发推送-jpush
        // appPage: /pages/reserve/reserveMyOrder?activeTab=C

        String personTel = reserveGoodsConfirmOrderDtos.get(0).getPersonTel();
        UserDto userDto = new UserDto();
        userDto.setTel(personTel);
        userDto.setStatusCd("0");
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        JPushMessageDto messageDto = new JPushMessageDto();
        messageDto.setCommunityId(reserveGoodsOrderTimeDto.getCommunityId());
        messageDto.setUserId(userDtos.get(0).getUserId());
        messageDto.setPageUrl("/pages/reserve/reserveMyOrder?activeTab=C");
        messageDto.setTitle("服务预约");
        messageDto.setContent("您的服务预约核销成功");
        messageDto.setPageType("appPage");
        JPushMessageQueue.addMsg(messageDto);

        UserNotificationPo userNotificationPo = new UserNotificationPo();
        userNotificationPo.setCommunityId(reserveGoodsOrderTimeDto.getCommunityId());
        userNotificationPo.setUserId(userDtos.get(0).getUserId());
        userNotificationPo.setNotificationId(0);
        userNotificationPo.setName("场地预约");
        userNotificationPo.setTitle("场地预约");
        userNotificationPo.setContent("您的场地预约核销成功");
        userNotificationPo.setPageType(0);
        userNotificationPo.setAppPageUrl("/pages/reserve/reserveMyOrder?activeTab=C");
        userNotificationPo.setH5PageUrl("");
        userNotificationPo.setBgImage("");
        userNotificationPo.setType(1);
        userNotificationInnerServiceSMOImpl.saveUserNotification(userNotificationPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(reserveGoodsConfirmOrderDtos));
    }
}
