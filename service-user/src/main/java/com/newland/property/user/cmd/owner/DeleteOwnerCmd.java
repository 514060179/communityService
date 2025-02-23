package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.account.AccountDto;
import com.newland.property.dto.data.DatabusDataDto;
import com.newland.property.dto.owner.OwnerAttrDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.owner.OwnerAppUserDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.intf.acct.IAccountInnerServiceSMO;
import com.newland.property.intf.job.IDataBusInnerServiceSMO;
import com.newland.property.intf.user.*;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.po.account.AccountPo;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.owner.OwnerAttrPo;
import com.newland.property.po.owner.OwnerPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmdDoc(title = "删除业主",
        description = "第三方系统，比如招商系统删除业主信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/owner.deleteOwner",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "owner.deleteOwner",
        seq = 11)

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
@NewlandPropertyCmd(serviceCode = "owner.deleteOwner")
public class DeleteOwnerCmd extends Cmd {

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
    private IOwnerAttrInnerServiceSMO ownerAttrInnerServiceSMOImpl;

    @Autowired
    private IAccountInnerServiceSMO accountInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();
        Assert.jsonObjectHaveKey(reqJson, "memberId", "请求报文中未包含memberId");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId");
        //todo 如果是业主
        if (OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) {
            //ownerId 写为 memberId
            reqJson.put("ownerId", reqJson.getString("memberId"));
            RoomDto roomDto = new RoomDto();
            roomDto.setOwnerId(reqJson.getString("ownerId"));
            List<RoomDto> roomDtoList = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);
            if (roomDtoList.size() > 0) {
                throw new IllegalArgumentException("删除失败,删除前请先解绑房屋信息");
            }
            //查询车位信息
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setOwnerId(reqJson.getString("ownerId"));
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            if (ownerCarDtos.size() > 0) {
                throw new IllegalArgumentException("删除失败,删除前请先解绑车位信息");
            }
            //小区楼添加到小区中
            //ownerBMOImpl.exitCommunityMember(reqJson, context);
        }
        if (!OwnerDto.OWNER_TYPE_CD_OWNER.equals(reqJson.getString("ownerTypeCd"))) { //不是业主成员不管
            return;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(reqJson.getString("memberId"));
        ownerDto.setCommunityId(reqJson.getString("communityId"));
        ownerDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER, OwnerDto.OWNER_TYPE_CD_RENTING});
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);
        if (ownerDtos != null && ownerDtos.size() > 0) {
            throw new IllegalArgumentException("请先删除业主下的成员");
        }
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

        //删除绑定业主信息
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

        //删除业主属性表信息
        OwnerAttrDto ownerAttrDto = new OwnerAttrDto();
        ownerAttrDto.setMemberId(reqJson.getString("memberId"));
        List<OwnerAttrDto> ownerAttrDtos = ownerAttrInnerServiceSMOImpl.queryOwnerAttrs(ownerAttrDto);
        if (ownerAttrDtos != null && ownerAttrDtos.size() > 0) {
            for (OwnerAttrDto ownerAttr : ownerAttrDtos) {
                OwnerAttrPo ownerAttrPo = new OwnerAttrPo();
                ownerAttrPo.setAttrId(ownerAttr.getAttrId());
                ownerAttrPo.setStatusCd("1"); //0 可用，1 不可用
                int i = ownerAttrInnerServiceSMOImpl.updateOwnerAttrInfoInstance(ownerAttrPo);
                if (i < 1) {
                    throw new CmdException("删除业主属性表失败");
                }
            }
        }

        //删除业主账户信息
        AccountDto accountDto = new AccountDto();
        accountDto.setObjId(reqJson.getString("memberId"));
        List<AccountDto> accountDtos = accountInnerServiceSMOImpl.queryAccounts(accountDto);
        if (accountDtos != null && accountDtos.size() > 0) {
            for (AccountDto account : accountDtos) {
                AccountPo accountPo = new AccountPo();
                accountPo.setAcctId(account.getAcctId());
                accountPo.setStatusCd("1"); //0 可用，1 不可用
                int i = accountInnerServiceSMOImpl.updateAccount(accountPo);
                if (i < 1) {
                    throw new CmdException("删除业主账户信息失败");
                }
            }
        }

        // todo 同步业主信息到iot
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(BusinessTypeConstant.BUSINESS_TYPE_DELETE_OWNER_TO_IOT, reqJson));
    }
}
