package com.newland.property.fee.bmo.feeDiscountRuleSpec;
import com.newland.property.dto.fee.FeeDiscountRuleSpecDto;
import org.springframework.http.ResponseEntity;
public interface IGetFeeDiscountRuleSpecBMO {


    /**
     * 查询折扣规则配置
     * add by wuxw
     * @param  feeDiscountRuleSpecDto
     * @return
     */
    ResponseEntity<String> get(FeeDiscountRuleSpecDto feeDiscountRuleSpecDto);


}
