package com.newland.property.store.bmo.purchase;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IPurchaseApplyBMO {

    /**
     * 采购申请
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> apply(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson);
}
