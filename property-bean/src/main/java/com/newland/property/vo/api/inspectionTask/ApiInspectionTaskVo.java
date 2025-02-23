package com.newland.property.vo.api.inspectionTask;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionTaskVo extends MorePageVo implements Serializable {
    List<ApiInspectionTaskDataVo> inspectionTasks;


    public List<ApiInspectionTaskDataVo> getInspectionTasks() {
        return inspectionTasks;
    }

    public void setInspectionTasks(List<ApiInspectionTaskDataVo> inspectionTasks) {
        this.inspectionTasks = inspectionTasks;
    }
}
