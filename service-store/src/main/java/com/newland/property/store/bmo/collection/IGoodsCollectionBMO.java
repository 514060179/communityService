package com.newland.property.store.bmo.collection;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IGoodsCollectionBMO {

    /**
     * 物品领用
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> collection(PurchaseApplyPo purchaseApplyPo, JSONObject reqJson);
}
