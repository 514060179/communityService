package com.newland.property.fee.bmo.feeManualCollectionDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeManualCollectionDetail.IDeleteFeeManualCollectionDetailBMO;
import com.newland.property.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeManualCollectionDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeManualCollectionDetailBMOImpl")
public class DeleteFeeManualCollectionDetailBMOImpl implements IDeleteFeeManualCollectionDetailBMO {

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(FeeManualCollectionDetailPo feeManualCollectionDetailPo) {

        int flag = feeManualCollectionDetailInnerServiceSMOImpl.deleteFeeManualCollectionDetail(feeManualCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
