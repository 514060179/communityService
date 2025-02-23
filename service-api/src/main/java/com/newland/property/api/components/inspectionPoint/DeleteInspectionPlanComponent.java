package com.newland.property.api.components.inspectionPoint;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.inspection.IDeleteInspectionPlanSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检计划组件
 */
@Component("deleteInspectionPlan")
public class DeleteInspectionPlanComponent {

    @Autowired
    private IDeleteInspectionPlanSMO deleteInspectionPlanSMOImpl;

    /**
     * 添加巡检计划数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteInspectionPlanSMOImpl.deleteInspectionPlan(pd);
    }

    public IDeleteInspectionPlanSMO getDeleteInspectionPlanSMOImpl() {
        return deleteInspectionPlanSMOImpl;
    }

    public void setDeleteInspectionPlanSMOImpl(IDeleteInspectionPlanSMO deleteInspectionPlanSMOImpl) {
        this.deleteInspectionPlanSMOImpl = deleteInspectionPlanSMOImpl;
    }
}
