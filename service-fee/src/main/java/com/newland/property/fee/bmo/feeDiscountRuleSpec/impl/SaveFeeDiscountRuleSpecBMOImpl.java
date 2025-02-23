package com.newland.property.fee.bmo.feeDiscountRuleSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeDiscountRuleSpec.ISaveFeeDiscountRuleSpecBMO;
import com.newland.property.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountRuleSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeDiscountRuleSpecBMOImpl")
public class SaveFeeDiscountRuleSpecBMOImpl implements ISaveFeeDiscountRuleSpecBMO {

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeDiscountRuleSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeDiscountRuleSpecPo feeDiscountRuleSpecPo) {

        feeDiscountRuleSpecPo.setSpecId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_specId));
        int flag = feeDiscountRuleSpecInnerServiceSMOImpl.saveFeeDiscountRuleSpec(feeDiscountRuleSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
