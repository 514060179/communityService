package com.newland.property.vo.api.inspectionPoint;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionPointVo extends MorePageVo implements Serializable {
    List<ApiInspectionPointDataVo> inspectionPoints;


    public List<ApiInspectionPointDataVo> getInspectionPoints() {
        return inspectionPoints;
    }

    public void setInspectionPoints(List<ApiInspectionPointDataVo> inspectionPoints) {
        this.inspectionPoints = inspectionPoints;
    }
}
