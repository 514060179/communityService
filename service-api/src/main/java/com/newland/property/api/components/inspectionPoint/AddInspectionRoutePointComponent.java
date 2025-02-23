package com.newland.property.api.components.inspectionPoint;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.inspection.IAddInspectionRoutePointSMO;
import com.newland.property.api.smo.inspection.IListInspectionPointsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检路线巡检点组件
 */
@Component("addInspectionRoutePoint")
public class AddInspectionRoutePointComponent {

    @Autowired
    private IAddInspectionRoutePointSMO addInspectionRoutePointSMO;


    @Autowired
    private IListInspectionPointsSMO listInspectionPointsSMOImpl;

    /**
     * 添加巡检路线数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addInspectionRoutePointSMO.saveInspectionRoutePoint(pd);
    }

    public ResponseEntity<String> list(IPageData pd){
        return listInspectionPointsSMOImpl.listInspectionPoints(pd);
    }


    public IAddInspectionRoutePointSMO getAddInspectionRoutePointSMO() {
        return addInspectionRoutePointSMO;
    }

    public void setAddInspectionRoutePointSMO(IAddInspectionRoutePointSMO addInspectionRoutePointSMO) {
        this.addInspectionRoutePointSMO = addInspectionRoutePointSMO;
    }

    public IListInspectionPointsSMO getListInspectionPointsSMOImpl() {
        return listInspectionPointsSMOImpl;
    }

    public void setListInspectionPointsSMOImpl(IListInspectionPointsSMO listInspectionPointsSMOImpl) {
        this.listInspectionPointsSMOImpl = listInspectionPointsSMOImpl;
    }
}
