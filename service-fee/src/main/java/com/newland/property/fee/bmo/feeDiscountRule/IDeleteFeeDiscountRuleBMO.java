package com.newland.property.fee.bmo.feeDiscountRule;

import com.newland.property.po.fee.FeeDiscountRulePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeDiscountRuleBMO {


    /**
     * 修改费用折扣规则
     * add by wuxw
     *
     * @param feeDiscountRulePo
     * @return
     */
    ResponseEntity<String> delete(FeeDiscountRulePo feeDiscountRulePo);


}
