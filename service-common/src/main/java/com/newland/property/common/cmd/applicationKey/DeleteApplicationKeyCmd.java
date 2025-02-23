package com.newland.property.common.cmd.applicationKey;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.common.IApplicationKeyV1InnerServiceSMO;
import com.newland.property.po.accessControl.ApplicationKeyPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "applicationKey.deleteApplicationKey")
public class DeleteApplicationKeyCmd extends Cmd{

    @Autowired
    private IApplicationKeyV1InnerServiceSMO applicationKeyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        //Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区");

        Assert.hasKeyAndValue(reqJson, "applicationKeyId", "钥匙申请ID不能为空");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ApplicationKeyPo applicationKeyPo = BeanConvertUtil.covertBean(reqJson, ApplicationKeyPo.class);
        int flag = applicationKeyV1InnerServiceSMOImpl.deleteApplicationKey(applicationKeyPo);

        if (flag < 1) {
            throw new CmdException("保存开门记录失败");
        }
    }
}
