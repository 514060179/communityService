package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.owner.OwnerAppUserDto;
import com.newland.property.dto.user.UserAttrDto;
import com.newland.property.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserAttrV1InnerServiceSMO;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.user.UserAttrPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "owner.refreshAppUserBindingOwnerOpenId")
public class RefreshAppUserBindingOwnerOpenIdCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserAttrV1InnerServiceSMO userAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "openId", "必填，请填写状态");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        if (StringUtil.isEmpty(userId)) {
            throw new CmdException("用户未登录");
        }

        //todo 刷user_attr 中的openId
        freshUserToken(reqJson, userId);

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setUserId(userId);
        ownerAppUserDto.setCommunityId(reqJson.getString("communityId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        if (ListUtil.isNull(ownerAppUserDtos)) {
            return;
        }
        OwnerAppUserPo ownerAppUserPo = new OwnerAppUserPo();
        ownerAppUserPo.setAppUserId(ownerAppUserDtos.get(0).getAppUserId());
        ownerAppUserPo.setCommunityId(reqJson.getString("communityId"));
        ownerAppUserPo.setOpenId(reqJson.getString("openId"));
        ownerAppUserPo.setNickName(reqJson.getString("nickName"));
        ownerAppUserPo.setHeadImgUrl(reqJson.getString("headImgUrl"));
        ownerAppUserV1InnerServiceSMOImpl.updateOwnerAppUser(ownerAppUserPo);
    }

    private void freshUserToken(JSONObject reqJson, String userId) {
        //判断 用户下是否有openId 属性有修改没有添加
        if (!reqJson.containsKey("openId")) {
            return;
        }


        UserAttrDto userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(userId);
        userAttrDto.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        List<UserAttrDto> userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);
        if (ListUtil.isNull(userAttrDtos)) {
            UserAttrPo userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(userId);
            userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            userAttrPo.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
            userAttrPo.setValue(reqJson.getString("openId"));
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
            return;
        }
        UserAttrPo userAttrPo = new UserAttrPo();
        userAttrPo.setUserId(userId);
        userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
        userAttrPo.setSpecCd(UserAttrDto.SPEC_OPEN_ID);
        userAttrPo.setValue(reqJson.getString("openId"));
        userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);

        if (!reqJson.containsKey("unionId")) {
            return;
        }

        userAttrDto = new UserAttrDto();
        userAttrDto.setUserId(userId);
        userAttrDto.setSpecCd(UserAttrDto.SPEC_UNION_ID);
        userAttrDtos = userAttrV1InnerServiceSMOImpl.queryUserAttrs(userAttrDto);
        if (ListUtil.isNull(userAttrDtos)) {
            userAttrPo = new UserAttrPo();
            userAttrPo.setUserId(userId);
            userAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            userAttrPo.setSpecCd(UserAttrDto.SPEC_UNION_ID);
            userAttrPo.setValue(reqJson.getString("unionId"));
            userAttrV1InnerServiceSMOImpl.saveUserAttr(userAttrPo);
            return;
        }
        userAttrPo = new UserAttrPo();
        userAttrPo.setUserId(userId);
        userAttrPo.setAttrId(userAttrDtos.get(0).getAttrId());
        userAttrPo.setSpecCd(UserAttrDto.SPEC_UNION_ID);
        userAttrPo.setValue(reqJson.getString("unionId"));
        userAttrV1InnerServiceSMOImpl.updateUserAttr(userAttrPo);
    }
}
