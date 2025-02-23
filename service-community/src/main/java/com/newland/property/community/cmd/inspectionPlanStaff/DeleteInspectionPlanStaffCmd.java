package com.newland.property.community.cmd.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.intf.community.IInspectionPlanStaffV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionPlanStaffPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "inspectionPlanStaff.deleteInspectionPlanStaff")
public class DeleteInspectionPlanStaffCmd extends Cmd {

    @Autowired
    private IInspectionPlanStaffV1InnerServiceSMO inspectionPlanStaffV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        Assert.hasKeyAndValue(reqJson, "ipStaffId", "ipStaffId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区信息不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanStaffPo inspectionPlanStaffPo = BeanConvertUtil.covertBean(reqJson, InspectionPlanStaffPo.class);
        int flag = inspectionPlanStaffV1InnerServiceSMOImpl.deleteInspectionPlanStaff(inspectionPlanStaffPo);
        if (flag < 1) {
            throw new CmdException("删除巡检师傅失败");
        }
    }
}
