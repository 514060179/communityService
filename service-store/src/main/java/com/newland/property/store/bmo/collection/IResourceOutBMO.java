package com.newland.property.store.bmo.collection;

import com.newland.property.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IResourceOutBMO {

    /**
     * 采购入库
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> out(PurchaseApplyPo purchaseApplyPo);
}
