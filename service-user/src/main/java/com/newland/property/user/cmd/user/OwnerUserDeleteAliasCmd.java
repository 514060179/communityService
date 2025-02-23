package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.dto.user.UserAliasDto;
import com.newland.property.intf.user.IUserAliasInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

@NewlandPropertyCmdDoc(title = "删除注册alias",
        description = "在客户端退出登录后删除极光别名成功，调用后端接口伤处用户注册记录",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/user.ownerUserDeleteAlias",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "user.ownerUserDeleteAlias",
        seq = 101
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "userId", length = 30, remark = "用户ID"),
        @NewlandPropertyParamDoc(name = "platform", length = 64, remark = "平台 ios android")
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\"userId\":\"302024061440240356\",\"platform\":\"ios\"}",
        resBody = "{'code':0,'msg':'成功'}"
)

/*
  @author Moonny
 */
@NewlandPropertyCmd(serviceCode = "user.ownerUserDeleteAlias")
public class OwnerUserDeleteAliasCmd extends Cmd {

    @Autowired
    private IUserAliasInnerServiceSMO userAliasInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "platform", "请求报文中未包含platform");
        Assert.hasKeyAndValue(reqJson, "userId", "userId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        UserAliasDto userAliasDto = BeanConvertUtil.covertBean(reqJson, UserAliasDto.class);
        userAliasDto.setStatusCd(1);
        int flag = userAliasInnerServiceSMOImpl.updateUserAlias(userAliasDto);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        context.setResponseEntity(ResultVo.success());
    }
}
