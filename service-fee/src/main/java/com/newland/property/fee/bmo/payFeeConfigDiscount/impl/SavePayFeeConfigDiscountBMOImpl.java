package com.newland.property.fee.bmo.payFeeConfigDiscount.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.payFee.PayFeeConfigDiscountDto;
import com.newland.property.fee.bmo.payFeeConfigDiscount.ISavePayFeeConfigDiscountBMO;
import com.newland.property.intf.fee.IPayFeeConfigDiscountInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeConfigDiscountPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePayFeeConfigDiscountBMOImpl")
public class SavePayFeeConfigDiscountBMOImpl implements ISavePayFeeConfigDiscountBMO {

    @Autowired
    private IPayFeeConfigDiscountInnerServiceSMO payFeeConfigDiscountInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param payFeeConfigDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(PayFeeConfigDiscountPo payFeeConfigDiscountPo) {

        PayFeeConfigDiscountDto payFeeConfigDiscountDto = new PayFeeConfigDiscountDto();
        payFeeConfigDiscountDto.setConfigId(payFeeConfigDiscountPo.getConfigId());
        payFeeConfigDiscountDto.setDiscountId(payFeeConfigDiscountPo.getDiscountId());
        int i = payFeeConfigDiscountInnerServiceSMOImpl.queryPayFeeConfigDiscountsCount(payFeeConfigDiscountDto);
        if (i > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败，不能添加相同的折扣！");
        }

        payFeeConfigDiscountPo.setConfigDiscountId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_configDiscountId));
        int flag = payFeeConfigDiscountInnerServiceSMOImpl.savePayFeeConfigDiscount(payFeeConfigDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
