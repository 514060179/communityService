package com.newland.property.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionPlanDto;
import com.newland.property.intf.community.IInspectionPlanInnerServiceSMO;
import com.newland.property.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionPlanPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionPlan.updateInspectionPlanState")
public class UpdateInspectionPlanStateCmd extends Cmd {

    @Autowired
    private IInspectionPlanInnerServiceSMO inspectionPlanInnerServiceSMOImpl;

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionPlanId", "inspectionPlanId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        Assert.hasKeyAndValue(reqJson, "state", "必填，请填写状态");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        InspectionPlanDto inspectionPlanDto = new InspectionPlanDto();
        inspectionPlanDto.setCommunityId(reqJson.getString("communityId"));
        inspectionPlanDto.setInspectionPlanId(reqJson.getString("inspectionPlanId"));
        List<InspectionPlanDto> inspectionPlanDtos = inspectionPlanInnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto);

        Assert.listOnlyOne(inspectionPlanDtos, "根据计划ID查询到多条记录，请检查数据");
        JSONObject businessInspectionPlan = new JSONObject();
        businessInspectionPlan.putAll(BeanConvertUtil.beanCovertMap(inspectionPlanDtos.get(0)));
        businessInspectionPlan.put("state", reqJson.getString("state"));
        InspectionPlanPo inspectionPlanPo = BeanConvertUtil.covertBean(businessInspectionPlan, InspectionPlanPo.class);
        int flag = inspectionPlanV1InnerServiceSMOImpl.updateInspectionPlan(inspectionPlanPo);
        if (flag < 1) {
            throw new CmdException("修改巡检计划失败");
        }
    }
}
