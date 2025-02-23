package com.newland.property.vo.api.serviceRegister;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiServiceRegisterVo extends MorePageVo implements Serializable {
    List<ApiServiceRegisterDataVo> serviceRegisters;


    public List<ApiServiceRegisterDataVo> getServiceRegisters() {
        return serviceRegisters;
    }

    public void setServiceRegisters(List<ApiServiceRegisterDataVo> serviceRegisters) {
        this.serviceRegisters = serviceRegisters;
    }
}
