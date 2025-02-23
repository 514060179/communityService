package com.newland.property.fee.bmo.feeManualCollection.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeManualCollection.IDeleteFeeManualCollectionBMO;
import com.newland.property.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.newland.property.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.newland.property.po.fee.FeeManualCollectionPo;
import com.newland.property.po.fee.FeeManualCollectionDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteFeeManualCollectionBMOImpl")
public class DeleteFeeManualCollectionBMOImpl implements IDeleteFeeManualCollectionBMO {

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;
    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(FeeManualCollectionPo feeManualCollectionPo) {

        FeeManualCollectionDetailPo feeManualCollectionDetailPo = new FeeManualCollectionDetailPo();
        feeManualCollectionDetailPo.setCollectionId(feeManualCollectionPo.getCollectionId());

        feeManualCollectionDetailInnerServiceSMOImpl.deleteFeeManualCollectionDetail(feeManualCollectionDetailPo);

        int flag = feeManualCollectionInnerServiceSMOImpl.deleteFeeManualCollection(feeManualCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
