package com.newland.property.community.cmd.inspectionTask;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.community.IInspectionTaskV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionTaskPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "inspectionTask.saveInspectionTask")
public class SaveInspectionTaskCmd extends Cmd {

    @Autowired
    private IInspectionTaskV1InnerServiceSMO inspectionTaskV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "请求报文中未包含inspectionPlanId");
        Assert.hasKeyAndValue(reqJson, "actInsTime", "请求报文中未包含actInsTime");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "actUserId", "请求报文中未包含actUserId");
        Assert.hasKeyAndValue(reqJson, "actUserName", "请求报文中未包含actUserName");
        Assert.hasKeyAndValue(reqJson, "signType", "请求报文中未包含signType");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        reqJson.put("taskId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_taskId));
        InspectionTaskPo inspectionTaskPo = BeanConvertUtil.covertBean(reqJson, InspectionTaskPo.class);
        int flag = inspectionTaskV1InnerServiceSMOImpl.saveInspectionTask(inspectionTaskPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
    }
}
