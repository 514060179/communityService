package com.newland.property.user.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.owner.OwnerAppUserDto;
import com.newland.property.intf.user.IOwnerAppUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.owner.OwnerAppUserPo;
import com.newland.property.po.user.UserPo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "owner.deleteAppUserBindingOwner")
public class DeleteAppUserBindingOwnerCmd extends Cmd {

    @Autowired
    private IOwnerAppUserV1InnerServiceSMO ownerAppUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "appUserId", "绑定ID不能为空");

        String env = MappingCache.getValue(MappingConstant.ENV_DOMAIN, "HC_ENV");

        if ("DEV".equals(env) || "TEST".equals(env)) {
            throw new IllegalArgumentException("演示环境不能解绑，解绑后演示账号手机端无法登陆");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        // todo 查询 绑定信息

        OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
        ownerAppUserDto.setAppUserId(reqJson.getString("appUserId"));
        List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserV1InnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);

        Assert.listOnlyOne(ownerAppUserDtos, "绑定信息不存在");

        // 删除绑定信息
        OwnerAppUserPo ownerAppUserPo = BeanConvertUtil.covertBean(reqJson, OwnerAppUserPo.class);
        int flag = ownerAppUserV1InnerServiceSMOImpl.deleteOwnerAppUser(ownerAppUserPo);
        if (flag < 1) {
            throw new CmdException("删除失败");
        }

        if (StringUtil.isEmpty(ownerAppUserDtos.get(0).getUserId())) {
            return;
        }
        if (ownerAppUserDtos.get(0).getUserId().startsWith("-")) {
            return;
        }
        // todo 删除用户信息
        UserPo userPo = new UserPo();
        userPo.setUserId(ownerAppUserDtos.get(0).getUserId());
        userV1InnerServiceSMOImpl.deleteUser(userPo);

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
