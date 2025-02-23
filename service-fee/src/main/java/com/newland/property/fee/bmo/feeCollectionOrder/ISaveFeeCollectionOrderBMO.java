package com.newland.property.fee.bmo.feeCollectionOrder;

import com.newland.property.po.fee.FeeCollectionOrderPo;
import org.springframework.http.ResponseEntity;
public interface ISaveFeeCollectionOrderBMO {


    /**
     * 添加催缴单
     * add by wuxw
     * @param feeCollectionOrderPo
     * @return
     */
    ResponseEntity<String> save(FeeCollectionOrderPo feeCollectionOrderPo);


}
