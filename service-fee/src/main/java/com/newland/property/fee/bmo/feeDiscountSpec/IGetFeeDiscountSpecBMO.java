package com.newland.property.fee.bmo.feeDiscountSpec;

import com.newland.property.dto.fee.FeeDiscountSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetFeeDiscountSpecBMO {


    /**
     * 查询费用折扣
     * add by wuxw
     *
     * @param feeDiscountSpecDto
     * @return
     */
    ResponseEntity<String> get(FeeDiscountSpecDto feeDiscountSpecDto);


}
