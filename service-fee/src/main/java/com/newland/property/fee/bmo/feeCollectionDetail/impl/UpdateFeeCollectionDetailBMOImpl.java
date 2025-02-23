package com.newland.property.fee.bmo.feeCollectionDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeCollectionDetail.IUpdateFeeCollectionDetailBMO;
import com.newland.property.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeCollectionDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeCollectionDetailBMOImpl")
public class UpdateFeeCollectionDetailBMOImpl implements IUpdateFeeCollectionDetailBMO {

    @Autowired
    private IFeeCollectionDetailInnerServiceSMO feeCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeCollectionDetailPo feeCollectionDetailPo) {

        int flag = feeCollectionDetailInnerServiceSMOImpl.updateFeeCollectionDetail(feeCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
