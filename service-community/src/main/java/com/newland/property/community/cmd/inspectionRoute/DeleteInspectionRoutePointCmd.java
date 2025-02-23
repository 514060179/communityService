package com.newland.property.community.cmd.inspectionRoute;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionRoutePointRelDto;
import com.newland.property.intf.community.IInspectionRoutePointRelInnerServiceSMO;
import com.newland.property.intf.community.IInspectionRoutePointRelV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionRoutePointRelPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionRoute.deleteInspectionRoutePoint")
public class DeleteInspectionRoutePointCmd extends Cmd {

    @Autowired
    private IInspectionRoutePointRelInnerServiceSMO inspectionRoutePointRelInnerServiceSMOImpl;

    @Autowired
    private IInspectionRoutePointRelV1InnerServiceSMO inspectionRoutePointRelV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "inspectionRouteId", "路线巡检路线不能为空");
        Assert.hasKeyAndValue(reqJson, "inspectionId", "路线巡检点不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionRoutePointRelDto inspectionRoutePointRelDto = new InspectionRoutePointRelDto();
        inspectionRoutePointRelDto.setCommunityId(reqJson.getString("communityId"));
        inspectionRoutePointRelDto.setInspectionId(reqJson.getString("inspectionId"));
        inspectionRoutePointRelDto.setInspectionRouteId(reqJson.getString("inspectionRouteId"));
        List<InspectionRoutePointRelDto> inspectionRoutePointRelDtos = inspectionRoutePointRelInnerServiceSMOImpl.queryInspectionRoutePointRels(inspectionRoutePointRelDto);

        Assert.listOnlyOne(inspectionRoutePointRelDtos, "未查询到（或多条）要删除的 巡检路线下的巡检点");
        JSONObject businessInspectionRoute = new JSONObject();
        businessInspectionRoute.putAll(BeanConvertUtil.beanCovertMap(inspectionRoutePointRelDtos.get(0)));

        InspectionRoutePointRelPo inspectionRoutePointRelPo = BeanConvertUtil.covertBean(businessInspectionRoute, InspectionRoutePointRelPo.class);

        int flag = inspectionRoutePointRelV1InnerServiceSMOImpl.deleteInspectionRoutePointRel(inspectionRoutePointRelPo);
        if (flag < 1) {
            throw new CmdException("删除巡检路线失败");
        }
    }
}
