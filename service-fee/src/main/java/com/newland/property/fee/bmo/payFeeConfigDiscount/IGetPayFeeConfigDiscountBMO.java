package com.newland.property.fee.bmo.payFeeConfigDiscount;

import com.newland.property.dto.payFee.PayFeeConfigDiscountDto;
import org.springframework.http.ResponseEntity;

public interface IGetPayFeeConfigDiscountBMO {


    /**
     * 查询费用项折扣
     * add by wuxw
     *
     * @param payFeeConfigDiscountDto
     * @return
     */
    ResponseEntity<String> get(PayFeeConfigDiscountDto payFeeConfigDiscountDto);


}
