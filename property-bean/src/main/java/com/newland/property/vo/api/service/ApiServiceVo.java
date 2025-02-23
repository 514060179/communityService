package com.newland.property.vo.api.service;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiServiceVo extends MorePageVo implements Serializable {
    List<ApiServiceDataVo> services;


    public List<ApiServiceDataVo> getServices() {
        return services;
    }

    public void setServices(List<ApiServiceDataVo> services) {
        this.services = services;
    }
}
