package com.newland.property.vo.api.purchaseApplyDetail;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiPurchaseApplyDetailVo extends MorePageVo implements Serializable {
    List<ApiPurchaseApplyDetailDataVo> purchaseApplyDetails;


    public List<ApiPurchaseApplyDetailDataVo> getPurchaseApplyDetails() {
        return purchaseApplyDetails;
    }

    public void setPurchaseApplyDetails(List<ApiPurchaseApplyDetailDataVo> purchaseApplyDetails) {
        this.purchaseApplyDetails = purchaseApplyDetails;
    }
}
