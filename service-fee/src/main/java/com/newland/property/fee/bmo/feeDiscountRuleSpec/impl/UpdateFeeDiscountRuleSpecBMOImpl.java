package com.newland.property.fee.bmo.feeDiscountRuleSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeDiscountRuleSpec.IUpdateFeeDiscountRuleSpecBMO;
import com.newland.property.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountRuleSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeDiscountRuleSpecBMOImpl")
public class UpdateFeeDiscountRuleSpecBMOImpl implements IUpdateFeeDiscountRuleSpecBMO {

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {

        int flag = feeDiscountRuleSpecInnerServiceSMOImpl.updateFeeDiscountRuleSpec(feeDiscountRuleSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
