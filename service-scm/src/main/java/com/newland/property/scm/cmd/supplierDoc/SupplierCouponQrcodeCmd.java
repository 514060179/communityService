package com.newland.property.scm.cmd.supplierDoc;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;

import java.text.ParseException;


@NewlandPropertyCmdDoc(title = "优惠券二维码",
        description = "调用第三方供应商系统获取优惠券二维码",
        httpMethod = "post",
        url = "admin 账户 供应商 自主设置",
        resource = "scmDoc",
        author = "吴学文",
        serviceCode = "supplier.supplierCouponQrcode"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "businessKey", length = 30, remark = "业务ID"),
        @NewlandPropertyParamDoc(name = "suppilerId", length = 30, remark = "供应商ID"),
        @NewlandPropertyParamDoc(name = "couponName", length = 30, remark = "优惠券名称"),
        @NewlandPropertyParamDoc(name = "couponId", length = 30, remark = "优惠券ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Object", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "qrCode", type = "String", remark = "二维码内容"),
                @NewlandPropertyParamDoc(parentNodeName = "data",name = "remark", type = "String", remark = "核销流程说明"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{'businessKey':'123123','suppilerId':'123123','couponName':'123123','couponId':'123123'}",
        resBody="{'code':0,'msg':'成功','data':{'qrCode':'123123','remark':7200}}"
)
@NewlandPropertyCmd(serviceCode = "supplier.supplierCouponQrcode")
public class SupplierCouponQrcodeCmd extends Cmd{
    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

    }
}
