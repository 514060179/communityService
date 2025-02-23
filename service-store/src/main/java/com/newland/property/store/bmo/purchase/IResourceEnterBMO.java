package com.newland.property.store.bmo.purchase;

import com.newland.property.po.purchase.PurchaseApplyPo;
import org.springframework.http.ResponseEntity;

public interface IResourceEnterBMO {

    /**
     * 采购入库
     * @param purchaseApplyPo
     * @return
     */
    ResponseEntity<String> enter(PurchaseApplyPo purchaseApplyPo);
}
