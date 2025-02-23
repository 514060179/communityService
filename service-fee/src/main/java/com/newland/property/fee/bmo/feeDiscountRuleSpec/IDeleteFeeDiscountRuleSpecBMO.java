package com.newland.property.fee.bmo.feeDiscountRuleSpec;
import com.newland.property.po.fee.FeeDiscountRuleSpecPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeDiscountRuleSpecBMO {


    /**
     * 修改折扣规则配置
     * add by wuxw
     * @param feeDiscountRuleSpecPo
     * @return
     */
    ResponseEntity<String> delete(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo);


}
