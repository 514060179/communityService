package com.newland.property.vo.api.visit;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiVisitVo extends MorePageVo implements Serializable {
    List<ApiVisitDataVo> visits;


    public List<ApiVisitDataVo> getVisits() {
        return visits;
    }

    public void setVisits(List<ApiVisitDataVo> visits) {
        this.visits = visits;
    }
}
