package com.newland.property.fee.bmo.feeReceipt.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeReceipt.IUpdateFeeReceiptBMO;
import com.newland.property.intf.fee.IFeeReceiptInnerServiceSMO;
import com.newland.property.po.fee.FeeReceiptPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeReceiptBMOImpl")
public class UpdateFeeReceiptBMOImpl implements IUpdateFeeReceiptBMO {

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    /**
     * @param feeReceiptPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeReceiptPo feeReceiptPo) {

        int flag = feeReceiptInnerServiceSMOImpl.updateFeeReceipt(feeReceiptPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
