package com.newland.property.vo.api.inspectionPlanStaff;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionPlanStaffVo extends MorePageVo implements Serializable {
    List<ApiInspectionPlanStaffDataVo> inspectionPlanStaffs;


    public List<ApiInspectionPlanStaffDataVo> getInspectionPlanStaffs() {
        return inspectionPlanStaffs;
    }

    public void setInspectionPlanStaffs(List<ApiInspectionPlanStaffDataVo> inspectionPlanStaffs) {
        this.inspectionPlanStaffs = inspectionPlanStaffs;
    }
}
