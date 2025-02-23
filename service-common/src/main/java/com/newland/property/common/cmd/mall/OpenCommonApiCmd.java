package com.newland.property.common.cmd.mall;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.common.bmo.mall.IMallCommonApiBmo;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.core.annotation.NewlandPropertyCmd;

/**
 * 专属于HC小区管理系统调用
 */
@NewlandPropertyCmd(serviceCode = "mall.openCommonApi")
public class OpenCommonApiCmd extends Cmd {

    private IMallCommonApiBmo mallCommonApiBmoImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "mallApiCode", "未包含MALL接口编码");


        mallCommonApiBmoImpl = ApplicationContextFactory.getBean(reqJson.getString("mallApiCode"), IMallCommonApiBmo.class);
        if (mallCommonApiBmoImpl == null) {
            throw new CmdException("未实现该能力");
        }

        mallCommonApiBmoImpl.validate(context, reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        mallCommonApiBmoImpl = ApplicationContextFactory.getBean(reqJson.getString("mallApiCode"), IMallCommonApiBmo.class);
        if (mallCommonApiBmoImpl == null) {
            throw new CmdException("未实现该能力");
        }
        mallCommonApiBmoImpl.doCmd(context, reqJson);
    }
}
