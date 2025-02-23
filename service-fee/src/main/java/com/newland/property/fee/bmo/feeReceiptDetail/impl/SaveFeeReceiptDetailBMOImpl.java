package com.newland.property.fee.bmo.feeReceiptDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeReceiptDetail.ISaveFeeReceiptDetailBMO;
import com.newland.property.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeReceiptDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeReceiptDetailBMOImpl")
public class SaveFeeReceiptDetailBMOImpl implements ISaveFeeReceiptDetailBMO {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeReceiptDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeReceiptDetailPo feeReceiptDetailPo) {

        feeReceiptDetailPo.setReceiptId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_receiptId));
        int flag = feeReceiptDetailInnerServiceSMOImpl.saveFeeReceiptDetail(feeReceiptDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
