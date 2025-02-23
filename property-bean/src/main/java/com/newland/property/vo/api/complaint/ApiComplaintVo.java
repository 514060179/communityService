package com.newland.property.vo.api.complaint;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiComplaintVo extends MorePageVo implements Serializable {
    List<ApiComplaintDataVo> complaints;


    public List<ApiComplaintDataVo> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<ApiComplaintDataVo> complaints) {
        this.complaints = complaints;
    }
}
