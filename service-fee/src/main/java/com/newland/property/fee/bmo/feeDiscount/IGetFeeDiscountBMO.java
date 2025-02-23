package com.newland.property.fee.bmo.feeDiscount;

import com.newland.property.dto.fee.FeeDiscountDto;
import com.newland.property.dto.payFee.PayFeeDetailDiscountDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeDiscountBMO {


    /**
     * 查询费用折扣
     * add by wuxw
     *
     * @param feeDiscountDto
     * @return
     */
    ResponseEntity<String> get(FeeDiscountDto feeDiscountDto);


    /**
     * 查询 缴费优惠
     * @param payFeeDetailDiscountDto
     * @return
     */
    ResponseEntity<String> getFeeDetailDiscount(PayFeeDetailDiscountDto payFeeDetailDiscountDto);
}
