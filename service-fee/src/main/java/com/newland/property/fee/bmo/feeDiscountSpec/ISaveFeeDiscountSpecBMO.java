package com.newland.property.fee.bmo.feeDiscountSpec;

import com.newland.property.po.fee.FeeDiscountSpecPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeDiscountSpecBMO {


    /**
     * 添加费用折扣
     * add by wuxw
     *
     * @param feeDiscountSpecPo
     * @return
     */
    ResponseEntity<String> save(FeeDiscountSpecPo feeDiscountSpecPo);


}
