package com.newland.property.fee.bmo.feeDiscount;

import com.newland.property.po.fee.FeeDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeDiscountBMO {


    /**
     * 修改费用折扣
     * add by wuxw
     *
     * @param feeDiscountPo
     * @return
     */
    ResponseEntity<String> delete(FeeDiscountPo feeDiscountPo);


}
