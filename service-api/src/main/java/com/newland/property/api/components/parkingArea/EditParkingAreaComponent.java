package com.newland.property.api.components.parkingArea;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.parkingArea.IEditParkingAreaSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editParkingArea")
public class EditParkingAreaComponent {

    @Autowired
    private IEditParkingAreaSMO editParkingAreaSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editParkingAreaSMOImpl.updateParkingArea(pd);
    }

    public IEditParkingAreaSMO getEditParkingAreaSMOImpl() {
        return editParkingAreaSMOImpl;
    }

    public void setEditParkingAreaSMOImpl(IEditParkingAreaSMO editParkingAreaSMOImpl) {
        this.editParkingAreaSMOImpl = editParkingAreaSMOImpl;
    }
}
