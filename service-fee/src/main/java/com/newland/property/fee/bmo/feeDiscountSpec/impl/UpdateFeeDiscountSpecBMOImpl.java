package com.newland.property.fee.bmo.feeDiscountSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeDiscountSpec.IUpdateFeeDiscountSpecBMO;
import com.newland.property.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeDiscountSpecBMOImpl")
public class UpdateFeeDiscountSpecBMOImpl implements IUpdateFeeDiscountSpecBMO {

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeDiscountSpecPo feeDiscountSpecPo) {

        int flag = feeDiscountSpecInnerServiceSMOImpl.updateFeeDiscountSpec(feeDiscountSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
