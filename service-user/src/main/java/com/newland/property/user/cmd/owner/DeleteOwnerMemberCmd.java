package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.owner.OwnerAppUserDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.jpush.ISendJPushMessageAdapt;
import com.newland.property.intf.user.*;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmdDoc(title = "删除业主成员",
        description = "第三方系统，比如招商系统删除业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.deleteOwnerMember",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.deleteOwnerMember",
        seq = 11
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "memberId", length = 30, remark = "业主ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\n" +
                "\t\"memberId\": 123123123,\n" +
                "\t\"communityId\": \"2022121921870161\"\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
@NewlandPropertyCmd(serviceCode = "owner.deleteOwnerMember")
public class DeleteOwnerMemberCmd extends Cmd {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("memberId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER, OwnerDto.OWNER_TYPE_CD_RENTING,
                OwnerDto.OWNER_TYPE_CD_TEMP, OwnerDto.OWNER_TYPE_CD_OTHER});

        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主成员不存在");


    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        JSONObject businessOwner = new JSONObject();
        businessOwner.put("memberId", reqJson.getString("memberId"));
        businessOwner.put("communityId", reqJson.getString("communityId"));

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);
        int flag = ownerV1InnerServiceSMOImpl.deleteOwner(ownerPo);

        if (flag < 1) {
            throw new CmdException("删除失败");
        }
//        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) {
//            return;
//        }
        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setMemberId(reqJson.getString("memberId"));
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        //查询app用户表
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
        if (ownerAppUserDtos == null && ownerAppUserDtos.size() < 1) {
            return;
        }

        for (OwnerAppUserDto ownerAppUser : ownerAppUserDtos) {
            OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(ownerAppUser, OwnerAppUserPo.class);
            flag = ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);
            if (flag < 1) {
                throw new CmdException("删除失败");
            }

            //todo 应该删除用户
//            UserPo userPo = new UserPo();
//            userPo.setUserId(ownerAppUser.getUserId());
//            userV1InnerServiceSMOImpl.deleteUser(userPo);
        }

        // todo 删除成员成功发推送-jpush
        // appPage: /pages/family/familyList

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(reqJson.getString("ownerId"));
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        UserDto userDto = new UserDto();
        userDto.setTel(ownerDtos.get(0).getLink());
        userDto.setStatusCd("0");
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

//        JPushMessageDto messageDto = new JPushMessageDto();
//        messageDto.setCommunityId(reqJson.getString("communityId"));
//        messageDto.setUserId(userDtos.get(0).getUserId());
//        messageDto.setPageUrl("/pages/family/familyList");
//        messageDto.setTitle("家庭成员");
//        messageDto.setContent("删除家庭成员成功");
//        messageDto.setPageType("appPage");
//        JPushMessageQueue.addMsg(messageDto);

        ISendJPushMessageAdapt adapt = ApplicationContextFactory.getBean("deleteMemberSendMessageAdapt",
                ISendJPushMessageAdapt.class);
        if(adapt != null) {
            adapt.sendMessage(reqJson.getString("communityId"), userDtos.get(0).getUserId());
        }
    }
}
