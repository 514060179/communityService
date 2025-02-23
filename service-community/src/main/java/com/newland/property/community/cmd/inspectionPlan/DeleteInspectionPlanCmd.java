package com.newland.property.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionTaskDto;
import com.newland.property.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.newland.property.intf.community.IInspectionTaskInnerServiceSMO;
import com.newland.property.po.inspection.InspectionPlanPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionPlan.deleteInspectionPlan")
public class DeleteInspectionPlanCmd extends Cmd {

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "巡检计划名称不能为空");
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setInspectionPlanId(reqJson.getString("inspectionPlanId"));
        //根据巡检计划id查询巡检任务
        List<InspectionTaskDto> inspectionTaskDtos = inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto);
        Assert.listIsNull(inspectionTaskDtos, "该巡检计划正在使用，不能删除！");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(reqJson, InspectionPlanPo.class);
        int flag = inspectionPlanV1InnerServiceSMOImpl.deleteInspectionPlan(inspectionPlanPo);

        if (flag < 1) {
            throw new CmdException("删除巡检计划失败");
        }
    }
}
