package com.newland.property.fee.bmo.feeReceipt.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeReceipt.ISaveFeeReceiptBMO;
import com.newland.property.intf.fee.IFeeReceiptInnerServiceSMO;
import com.newland.property.po.fee.FeeReceiptPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeReceiptBMOImpl")
public class SaveFeeReceiptBMOImpl implements ISaveFeeReceiptBMO {

    @Autowired
    private IFeeReceiptInnerServiceSMO feeReceiptInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeReceiptPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeReceiptPo feeReceiptPo) {

        feeReceiptPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
        int flag = feeReceiptInnerServiceSMOImpl.saveFeeReceipt(feeReceiptPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
