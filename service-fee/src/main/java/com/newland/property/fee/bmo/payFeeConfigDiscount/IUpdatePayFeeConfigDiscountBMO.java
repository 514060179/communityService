package com.newland.property.fee.bmo.payFeeConfigDiscount;

import com.newland.property.po.payFee.PayFeeConfigDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePayFeeConfigDiscountBMO {


    /**
     * 修改费用项折扣
     * add by wuxw
     *
     * @param payFeeConfigDiscountPo
     * @return
     */
    ResponseEntity<String> update(PayFeeConfigDiscountPo payFeeConfigDiscountPo);


}
