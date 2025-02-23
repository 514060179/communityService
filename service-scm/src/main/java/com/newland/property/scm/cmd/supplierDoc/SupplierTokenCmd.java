package com.newland.property.scm.cmd.supplierDoc;


import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;

import java.text.ParseException;

@NewlandPropertyCmdDoc(title = "调用第三方供应商系统获取token",
        description = "appId 和 appSecure 还有 调用地址可以到admin账户添加供应商时设置",
        httpMethod = "post",
        url = "自主设置",
        resource = "scmDoc",
        author = "吴学文",
        serviceCode = "supplier.getToken"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "appId", length = 30, remark = "第三方系统APPID"),
        @NewlandPropertyParamDoc(name = "appSecure", length = 30, remark = "第三方系统appSecure"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "accessToken", type = "String", remark = "token"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "expiresIn", type = "String", remark = "过期时间（秒）"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{'appId':'123123','appSecure':'123123'}",
        resBody="{'code':0,'msg':'成功','data':{'accessToken':'123123','expiresIn':7200}}"
)
@NewlandPropertyCmd(serviceCode = "supplier.getToken")
public class SupplierTokenCmd extends Cmd{
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
