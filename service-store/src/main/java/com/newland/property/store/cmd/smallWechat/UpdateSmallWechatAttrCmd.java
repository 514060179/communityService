package com.newland.property.store.cmd.smallWechat;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.store.ISmallWechatAttrV1InnerServiceSMO;
import com.newland.property.po.wechat.SmallWechatAttrPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "smallWechat.updateSmallWechatAttr")
public class UpdateSmallWechatAttrCmd extends Cmd {

    @Autowired
    private ISmallWechatAttrV1InnerServiceSMO smallWechatAttrV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "attrId", "attrId不能为空");
        Assert.hasKeyAndValue(reqJson, "specCd", "请求报文中未包含specCd");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "value", "请求报文中未包含value");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        SmallWechatAttrPo smallWechatAttrPo = BeanConvertUtil.covertBean(reqJson, SmallWechatAttrPo.class);
        int flag = smallWechatAttrV1InnerServiceSMOImpl.updateSmallWechatAttr(smallWechatAttrPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        context.setResponseEntity(ResultVo.success());
    }
}
