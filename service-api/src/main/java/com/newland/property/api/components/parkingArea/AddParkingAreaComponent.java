package com.newland.property.api.components.parkingArea;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.parkingArea.IAddParkingAreaSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加停车场组件
 */
@Component("addParkingArea")
public class AddParkingAreaComponent {

    @Autowired
    private IAddParkingAreaSMO addParkingAreaSMOImpl;

    /**
     * 添加停车场数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addParkingAreaSMOImpl.saveParkingArea(pd);
    }

    public IAddParkingAreaSMO getAddParkingAreaSMOImpl() {
        return addParkingAreaSMOImpl;
    }

    public void setAddParkingAreaSMOImpl(IAddParkingAreaSMO addParkingAreaSMOImpl) {
        this.addParkingAreaSMOImpl = addParkingAreaSMOImpl;
    }
}
