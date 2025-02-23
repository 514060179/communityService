package com.newland.property.vo.api.advert;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAdvertItemVo extends MorePageVo implements Serializable {
    List<ApiAdvertItemDataVo> advertItems;

    public List<ApiAdvertItemDataVo> getAdvertItems() {
        return advertItems;
    }

    public void setAdvertItems(List<ApiAdvertItemDataVo> advertItems) {
        this.advertItems = advertItems;
    }
}
