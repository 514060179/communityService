package com.newland.property.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.community.IInspectionRouteV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionRoutePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "inspectionRoute.updateInspectionRoute")
public class UpdateInspectionRouteCmd extends Cmd {


    @Autowired
    private IInspectionRouteV1InnerServiceSMO inspectionRouteV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "路线ID不能为空");
        Assert.hasKeyAndValue(reqJson, "routeName", "必填，请填写路线名称，字数100个以内");
        Assert.hasKeyAndValue(reqJson, "seq", "必填，请选择巡点名称");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRoutePo inspectionRoutePo = BeanConvertUtil.covertBean(reqJson, InspectionRoutePo.class);

        int flag = inspectionRouteV1InnerServiceSMOImpl.updateInspectionRoute(inspectionRoutePo);
        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
