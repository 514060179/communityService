package com.newland.property.fee.bmo.feeDiscountSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeDiscountSpec.ISaveFeeDiscountSpecBMO;
import com.newland.property.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeDiscountSpecBMOImpl")
public class SaveFeeDiscountSpecBMOImpl implements ISaveFeeDiscountSpecBMO {

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeDiscountSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeDiscountSpecPo feeDiscountSpecPo) {

        feeDiscountSpecPo.setSpecId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_specId));
        int flag = feeDiscountSpecInnerServiceSMOImpl.saveFeeDiscountSpec(feeDiscountSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
