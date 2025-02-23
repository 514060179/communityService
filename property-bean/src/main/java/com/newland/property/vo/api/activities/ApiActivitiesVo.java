package com.newland.property.vo.api.activities;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiActivitiesVo extends MorePageVo implements Serializable {
    List<ApiActivitiesDataVo> activitiess;


    public List<ApiActivitiesDataVo> getActivitiess() {
        return activitiess;
    }

    public void setActivitiess(List<ApiActivitiesDataVo> activitiess) {
        this.activitiess = activitiess;
    }
}
