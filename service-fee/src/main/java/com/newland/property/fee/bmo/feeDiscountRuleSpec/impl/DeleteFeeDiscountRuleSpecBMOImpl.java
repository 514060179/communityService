package com.newland.property.fee.bmo.feeDiscountRuleSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeDiscountRuleSpec.IDeleteFeeDiscountRuleSpecBMO;
import com.newland.property.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountRuleSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeDiscountRuleSpecBMOImpl")
public class DeleteFeeDiscountRuleSpecBMOImpl implements IDeleteFeeDiscountRuleSpecBMO {

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {

        int flag = feeDiscountRuleSpecInnerServiceSMOImpl.deleteFeeDiscountRuleSpec(feeDiscountRuleSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
