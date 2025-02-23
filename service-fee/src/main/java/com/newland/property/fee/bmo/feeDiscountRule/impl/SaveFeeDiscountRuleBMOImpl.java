package com.newland.property.fee.bmo.feeDiscountRule.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeDiscountRule.ISaveFeeDiscountRuleBMO;
import com.newland.property.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountRulePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeDiscountRuleBMOImpl")
public class SaveFeeDiscountRuleBMOImpl implements ISaveFeeDiscountRuleBMO {

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeDiscountRulePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeDiscountRulePo feeDiscountRulePo) {

        feeDiscountRulePo.setRuleId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ruleId));
        int flag = feeDiscountRuleInnerServiceSMOImpl.saveFeeDiscountRule(feeDiscountRulePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
