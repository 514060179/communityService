package com.newland.property.fee.bmo.feeDiscountRule;

import com.newland.property.po.fee.FeeDiscountRulePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeDiscountRuleBMO {


    /**
     * 修改费用折扣规则
     * add by wuxw
     *
     * @param feeDiscountRulePo
     * @return
     */
    ResponseEntity<String> update(FeeDiscountRulePo feeDiscountRulePo);


}
