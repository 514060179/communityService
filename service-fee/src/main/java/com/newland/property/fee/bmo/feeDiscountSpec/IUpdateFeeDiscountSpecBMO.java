package com.newland.property.fee.bmo.feeDiscountSpec;

import com.newland.property.po.fee.FeeDiscountSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeDiscountSpecBMO {


    /**
     * 修改费用折扣
     * add by wuxw
     *
     * @param feeDiscountSpecPo
     * @return
     */
    ResponseEntity<String> update(FeeDiscountSpecPo feeDiscountSpecPo);


}
