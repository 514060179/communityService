package com.newland.property.vo.api.carInout;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiCarInoutVo extends MorePageVo implements Serializable {
    List<ApiCarInoutDataVo> carInouts;


    public List<ApiCarInoutDataVo> getCarInouts() {
        return carInouts;
    }

    public void setCarInouts(List<ApiCarInoutDataVo> carInouts) {
        this.carInouts = carInouts;
    }
}
