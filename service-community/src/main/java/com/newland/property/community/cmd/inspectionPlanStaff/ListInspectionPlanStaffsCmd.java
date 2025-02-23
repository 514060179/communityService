package com.newland.property.community.cmd.inspectionPlanStaff;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionPlanStaffDto;
import com.newland.property.intf.community.IInspectionPlanStaffInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.inspectionPlanStaff.ApiInspectionPlanStaffDataVo;
import com.newland.property.vo.api.inspectionPlanStaff.ApiInspectionPlanStaffVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionPlanStaff.listInspectionPlanStaffs")
public class ListInspectionPlanStaffsCmd extends Cmd {


    @Autowired
    private IInspectionPlanStaffInnerServiceSMO inspectionPlanStaffInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        InspectionPlanStaffDto inspectionPlanStaffDto = BeanConvertUtil.covertBean(reqJson, InspectionPlanStaffDto.class);

        int count = inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffsCount(inspectionPlanStaffDto);

        List<ApiInspectionPlanStaffDataVo> inspectionPlanStaffs = null;

        if (count > 0) {
            inspectionPlanStaffs = BeanConvertUtil.covertBeanList(inspectionPlanStaffInnerServiceSMOImpl.queryInspectionPlanStaffs(inspectionPlanStaffDto), ApiInspectionPlanStaffDataVo.class);
        } else {
            inspectionPlanStaffs = new ArrayList<>();
        }

        ApiInspectionPlanStaffVo apiInspectionPlanStaffVo = new ApiInspectionPlanStaffVo();

        apiInspectionPlanStaffVo.setTotal(count);
        apiInspectionPlanStaffVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionPlanStaffVo.setInspectionPlanStaffs(inspectionPlanStaffs);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionPlanStaffVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
