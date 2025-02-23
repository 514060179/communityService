package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.doc.annotation.NewlandPropertyCmdDoc;
import com.newland.property.doc.annotation.NewlandPropertyExampleDoc;
import com.newland.property.doc.annotation.NewlandPropertyParamDoc;
import com.newland.property.doc.annotation.NewlandPropertyResponseDoc;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;


@NewlandPropertyCmdDoc(title = "生成用户二维码",
        description = "生成用户的唯一二维码信息",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/user.generatorUserQrCode",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "user.generatorUserQrCode",
        seq = 10
)


@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "String", length = 250, defaultValue = "二维码", remark = "二维码"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{}",
        resBody = "{'code':0,'msg':'成功','data':'sdsfsdf'}"
)

@NewlandPropertyCmd(serviceCode = "user.generatorUserQrCode")
public class GeneratorUserQrCodeCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String userId = context.getReqHeaders().get("user-id");

        String qrCode = userV1InnerServiceSMOImpl.generatorUserIdQrCode(userId);

        context.setResponseEntity(ResultVo.createResponseEntity(qrCode));
    }
}
