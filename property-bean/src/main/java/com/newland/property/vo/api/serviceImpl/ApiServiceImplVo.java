package com.newland.property.vo.api.serviceImpl;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiServiceImplVo extends MorePageVo implements Serializable {
    List<ApiServiceImplDataVo> serviceImpls;


    public List<ApiServiceImplDataVo> getServiceImpls() {
        return serviceImpls;
    }

    public void setServiceImpls(List<ApiServiceImplDataVo> serviceImpls) {
        this.serviceImpls = serviceImpls;
    }
}
