package com.newland.property.vo.api.area;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAreaVo extends MorePageVo implements Serializable {
    List<ApiAreaDataVo> areas;

    public List<ApiAreaDataVo> getAreas() {
        return areas;
    }

    public void setAreas(List<ApiAreaDataVo> areas) {
        this.areas = areas;
    }
}
