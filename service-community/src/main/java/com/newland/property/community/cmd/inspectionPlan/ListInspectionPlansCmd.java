package com.newland.property.community.cmd.inspectionPlan;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionPlanDto;
import com.newland.property.dto.inspection.InspectionRouteDto;
import com.newland.property.dto.org.OrgStaffRelDto;
import com.newland.property.intf.community.IInspectionPlanV1InnerServiceSMO;
import com.newland.property.intf.community.IInspectionRouteInnerServiceSMO;
import com.newland.property.intf.user.IOrgStaffRelInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.inspectionPlan.ApiInspectionPlanDataVo;
import com.newland.property.vo.api.inspectionPlan.ApiInspectionPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionPlan.listInspectionPlans")
public class ListInspectionPlansCmd extends Cmd {

    @Autowired
    private IInspectionPlanV1InnerServiceSMO inspectionPlanV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelInnerServiceSMO iOrgStaffRelInnerServiceSMO;

    @Autowired
    private IInspectionRouteInnerServiceSMO inspectionRouteInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionPlanDto inspectionPlanDto = BeanConvertUtil.covertBean(reqJson, InspectionPlanDto.class);

        int count = inspectionPlanV1InnerServiceSMOImpl.queryInspectionPlansCount(inspectionPlanDto);

        List<ApiInspectionPlanDataVo> inspectionPlans = null;

        if (count > 0) {
            inspectionPlans = BeanConvertUtil.covertBeanList(inspectionPlanV1InnerServiceSMOImpl.queryInspectionPlans(inspectionPlanDto), ApiInspectionPlanDataVo.class);
            List<Object> staffIds = new ArrayList<Object>();
            List<Object> inspectionRouteIds = new ArrayList<Object>();
            for (ApiInspectionPlanDataVo Plans : inspectionPlans) {
                staffIds.add(Plans.getStaffId());
                String[] ids = Plans.getInspectionRouteId().split(",");
                for (String s : ids) {
                    inspectionRouteIds.add(s);
                }
            }
            if (staffIds.size() > 0) {
                OrgStaffRelDto orgStaffRelDto = new OrgStaffRelDto();
                String[] staffIdsArray = new String[staffIds.size()];
                staffIds.toArray(staffIdsArray);
                orgStaffRelDto.setStaffIds(staffIdsArray);
                List<OrgStaffRelDto> orgStaffRelDtos = iOrgStaffRelInnerServiceSMO.queryOrgInfoByStaffIds(orgStaffRelDto);
                for (ApiInspectionPlanDataVo planDataVo : inspectionPlans) {
                    for (OrgStaffRelDto orgs : orgStaffRelDtos) {
                        if (planDataVo.getStaffId().equals(orgs.getStaffId())) {
                            planDataVo.setDepartmentId(orgs.getDepartmentId());
                            planDataVo.setDepartmentName(orgs.getDepartmentName());
                            planDataVo.setCompanyId(orgs.getCompanyId());
                            planDataVo.setCompanyName(orgs.getCompanyName());
                        }
                    }
                }
            }
            if (inspectionRouteIds.size() > 0) {
                //去重
                HashSet set = new HashSet(inspectionRouteIds);
                inspectionRouteIds.clear();
                inspectionRouteIds.addAll(set);
                InspectionRouteDto inspectionRouteDto = new InspectionRouteDto();
                String[] routeIds = (String[]) inspectionRouteIds.toArray(new String[inspectionRouteIds.size()]);
                inspectionRouteDto.setInspectionRouteIds(routeIds);
                List<InspectionRouteDto> inspectionRouteDtoList = inspectionRouteInnerServiceSMOImpl.queryInspectionRoutes(inspectionRouteDto);
                for (ApiInspectionPlanDataVo planDataVo : inspectionPlans) {
                    String[] routeIdArray = planDataVo.getInspectionRouteId().split(",");
                    for (String s : routeIdArray) {
                        for (InspectionRouteDto inspectionRouteDto1 : inspectionRouteDtoList) {
                            if (inspectionRouteDto1.getInspectionRouteId().equals(s)) {
                                if (planDataVo.getInspectionRouteName() == null) {
                                    planDataVo.setInspectionRouteName(inspectionRouteDto1.getRouteName());
                                } else {
                                    planDataVo.setInspectionRouteName(planDataVo.getInspectionRouteName() + "," + inspectionRouteDto1.getRouteName());
                                }
                            }
                        }

                    }
                }
            }
        } else {
            inspectionPlans = new ArrayList<>();
        }

        ApiInspectionPlanVo apiInspectionPlanVo = new ApiInspectionPlanVo();

        apiInspectionPlanVo.setTotal(count);
        apiInspectionPlanVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPlanVo.setInspectionPlans(inspectionPlans);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPlanVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
