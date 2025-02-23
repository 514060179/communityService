package com.newland.property.fee.bmo.payFeeConfigDiscount.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.payFeeConfigDiscount.IUpdatePayFeeConfigDiscountBMO;
import com.newland.property.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeConfigDiscountPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePayFeeConfigDiscountBMOImpl")
public class UpdatePayFeeConfigDiscountBMOImpl implements IUpdatePayFeeConfigDiscountBMO {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    /**
     * @param payFeeConfigDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(PayFeeConfigDiscountPo payFeeConfigDiscountPo) {

        int flag = payFeeConfigDiscountInnerServiceSMOImpl.updatePayFeeConfigDiscount(payFeeConfigDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
