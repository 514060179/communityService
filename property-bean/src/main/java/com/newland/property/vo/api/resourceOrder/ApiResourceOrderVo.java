package com.newland.property.vo.api.resourceOrder;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiResourceOrderVo extends MorePageVo implements Serializable {
    List<ApiResourceOrderDataVo> resourceOrders;


    public List<ApiResourceOrderDataVo> getResourceOrders() {
        return resourceOrders;
    }

    public void setResourceOrders(List<ApiResourceOrderDataVo> resourceOrders) {
        this.resourceOrders = resourceOrders;
    }
}
