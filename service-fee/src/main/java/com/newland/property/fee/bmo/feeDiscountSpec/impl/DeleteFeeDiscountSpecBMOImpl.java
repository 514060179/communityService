package com.newland.property.fee.bmo.feeDiscountSpec.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeDiscountSpec.IDeleteFeeDiscountSpecBMO;
import com.newland.property.intf.fee.IFeeDiscountSpecInnerServiceSMO;
import com.newland.property.po.fee.FeeDiscountSpecPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeDiscountSpecBMOImpl")
public class DeleteFeeDiscountSpecBMOImpl implements IDeleteFeeDiscountSpecBMO {

    @Autowired
    private IFeeDiscountSpecInnerServiceSMO feeDiscountSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountSpecPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(FeeDiscountSpecPo feeDiscountSpecPo) {

        int flag = feeDiscountSpecInnerServiceSMOImpl.deleteFeeDiscountSpec(feeDiscountSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
