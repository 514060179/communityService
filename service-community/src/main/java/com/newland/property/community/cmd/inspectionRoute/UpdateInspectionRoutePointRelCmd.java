package com.newland.property.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.community.IInspectionRoutePointRelV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionRoutePointRelPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "inspectionRoute.updateInspectionRoutePointRel")
public class UpdateInspectionRoutePointRelCmd extends Cmd {

    @Autowired
    private IInspectionRoutePointRelV1InnerServiceSMO inspectionRoutePointRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "irpRelId", "路线巡检点ID不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "路线ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRoutePointRelPo inspectionRoutePointRelPo = BeanConvertUtil.covertBean(reqJson, InspectionRoutePointRelPo.class);
        int flag = inspectionRoutePointRelV1InnerServiceSMOImpl.updateInspectionRoutePointRel(inspectionRoutePointRelPo);
        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
