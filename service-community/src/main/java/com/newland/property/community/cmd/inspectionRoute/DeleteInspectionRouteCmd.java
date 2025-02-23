package com.newland.property.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionPlanDto;
import com.newland.property.intf.community.IInspectionPlanInnerServiceSMO;
import com.newland.property.intf.community.IInspectionRouteV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionRoutePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionRoute.deleteInspectionRoute")
public class DeleteInspectionRouteCmd extends Cmd {

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionRouteV1InnerServiceSMO inspectionRouteV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
//Assert.hasKeyAndValue(reqJson, "xxx", "xxx");
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "路线ID不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();
        inspectionPlanDto.setInspectionRouteId(reqJson.getString("inspectionRouteId"));
        //根据巡检路线id查询巡检计划
        List<InspectionPlanDto> inspectionPlanDtos = inspectionPlanInnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto);
        Assert.listIsNull(inspectionPlanDtos, "该巡检路线正在使用，不能删除！");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRoutePo inspectionRoutePo = BeanConvertUtil.covertBean(reqJson, InspectionRoutePo.class);
        int flag = inspectionRouteV1InnerServiceSMOImpl.deleteInspectionRoute(inspectionRoutePo);

        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
