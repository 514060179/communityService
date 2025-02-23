package com.newland.property.fee.bmo.feeManualCollection.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeManualCollection.IUpdateFeeManualCollectionBMO;
import com.newland.property.intf.fee.IFeeManualCollectionInnerServiceSMO;
import com.newland.property.po.fee.FeeManualCollectionPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeManualCollectionBMOImpl")
public class UpdateFeeManualCollectionBMOImpl implements IUpdateFeeManualCollectionBMO {

    @Autowired
    private IFeeManualCollectionInnerServiceSMO feeManualCollectionInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeManualCollectionPo feeManualCollectionPo) {

        int flag = feeManualCollectionInnerServiceSMOImpl.updateFeeManualCollection(feeManualCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
