package com.newland.property.fee.bmo.feeReceiptDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeReceiptDetail.IUpdateFeeReceiptDetailBMO;
import com.newland.property.intf.fee.IFeeReceiptDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeReceiptDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeReceiptDetailBMOImpl")
public class UpdateFeeReceiptDetailBMOImpl implements IUpdateFeeReceiptDetailBMO {

    @Autowired
    private IFeeReceiptDetailInnerServiceSMO feeReceiptDetailInnerServiceSMOImpl;

    /**
     * @param feeReceiptDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeReceiptDetailPo feeReceiptDetailPo) {

        int flag = feeReceiptDetailInnerServiceSMOImpl.updateFeeReceiptDetail(feeReceiptDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
