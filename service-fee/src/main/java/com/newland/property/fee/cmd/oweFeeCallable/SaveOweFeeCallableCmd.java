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
package com.newland.property.fee.cmd.oweFeeCallable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.data.DatabusDataDto;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.oweFeeCallable.OweFeeCallableDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.wechat.SmallWeChatDto;
import com.newland.property.intf.job.IDataBusInnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：oweFeeCallable.saveOweFeeCallable
 * 请求路劲：/app/oweFeeCallable.SaveOweFeeCallable
 * add by 吴学文 at 2023-08-17 15:05:16 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "oweFeeCallable.saveOweFeeCallable")
public class SaveOweFeeCallableCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveOweFeeCallableCmd.class);

    public static final String CODE_PREFIX_ID = "10";


    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatV1InnerServiceSMO smallWechatV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "callableWay", "请求报文中未包含callableWay");

        if (!reqJson.containsKey("roomIds")) {
            throw new CmdException("未包含房屋信息");
        }

        JSONArray roomIds = reqJson.getJSONArray("roomIds");

        if (ListUtil.isNull(roomIds)) {
            throw new CmdException("未包含房屋信息");
        }

        if (!reqJson.containsKey("feeId") && !reqJson.containsKey("feeIds") && !reqJson.containsKey("configIds")) {
            throw new CmdException("未包含需要催缴的费用");
        }


        //todo 公众号校验
        if (OweFeeCallableDto.CALLABLE_WAY_WECHAT.equals(reqJson.getString("callableWay"))) {
            SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
            smallWeChatDto.setObjId(reqJson.getString("communityId"));
            smallWeChatDto.setWeChatType(SmallWeChatDto.WECHAT_TYPE_PUBLIC);
            smallWeChatDto.setWechatType(smallWeChatDto.WECHAT_TYPE_PUBLIC);
            int count = smallWechatV1InnerServiceSMOImpl.querySmallWechatsCount(smallWeChatDto);
            if (count < 1) {
                throw new CmdException("未配置公众号");
            }
        }

        //todo 短信暂不校验
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");

        reqJson.put("staffId", userId);
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_OWE_FEE_CALLABLE, reqJson));

//        OweFeeCallablePo oweFeeCallablePo = BeanConvertUtil.covertBean(reqJson, OweFeeCallablePo.class);
//        oweFeeCallablePo.setOfcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
//        int flag = oweFeeCallableV1InnerServiceSMOImpl.saveOweFeeCallable(oweFeeCallablePo);
//
//        if (flag < 1) {
//            throw new CmdException("保存数据失败");
//        }

        // todo 极光推送
        if (OweFeeCallableDto.CALLABLE_WAY_JPUSH.equals(reqJson.getString("callableWay"))) {
            // TODO 催缴信息发推送-jpush
            // appPage: pages/fee/oweFee

            JSONArray roomIds = reqJson.getJSONArray("roomIds");
            for (int i = 0; i < roomIds.size(); i++) {
                OwnerDto ownerDto = new OwnerDto();
                ownerDto.setRoomId(roomIds.getString(i));
                ownerDto.setCommunityId(reqJson.getString("communityId"));
                List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwnerLogsByRoom(ownerDto);

                OwnerDto ownerDto1 = new OwnerDto();
                ownerDto1.setMemberId(ownerDtoList.get(0).getOwnerId());
                List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto1);

                UserDto userDto = new UserDto();
                userDto.setTel(ownerDtos.get(0).getLink());
                userDto.setStatusCd("0");
                List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

                JPushMessageDto messageDto = new JPushMessageDto();
                messageDto.setCommunityId(reqJson.getString("communityId"));
                messageDto.setUserId(userDtos.get(0).getUserId());
                messageDto.setPageUrl("/pages/fee/oweFee");
                messageDto.setTitle("催缴信息");
                messageDto.setContent("您有尚未缴清的费用，请及时缴费");
                messageDto.setPageType("appPage");
                JPushMessageQueue.addMsg(messageDto);
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
