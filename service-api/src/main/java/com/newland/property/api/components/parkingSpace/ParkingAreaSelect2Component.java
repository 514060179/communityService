package com.newland.property.api.components.parkingSpace;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.parkingArea.IListParkingAreasSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加停车位组件
 */
@Component("parkingAreaSelect2")
public class ParkingAreaSelect2Component {

    @Autowired
    private IListParkingAreasSMO listParkingAreasSMOImpl;


    public ResponseEntity<String> list(IPageData pd) {

        return listParkingAreasSMOImpl.listParkingAreas(pd);
    }


    public IListParkingAreasSMO getListParkingAreasSMOImpl() {
        return listParkingAreasSMOImpl;
    }

    public void setListParkingAreasSMOImpl(IListParkingAreasSMO listParkingAreasSMOImpl) {
        this.listParkingAreasSMOImpl = listParkingAreasSMOImpl;
    }
}
