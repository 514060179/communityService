package com.newland.property.fee.bmo.feeCollectionOrder.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.feeCollectionOrder.IUpdateFeeCollectionOrderBMO;
import com.newland.property.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.newland.property.po.fee.FeeCollectionOrderPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateFeeCollectionOrderBMOImpl")
public class UpdateFeeCollectionOrderBMOImpl implements IUpdateFeeCollectionOrderBMO {

    @Autowired
    private IFeeCollectionOrderInnerServiceSMO feeCollectionOrderInnerServiceSMOImpl;

    /**
     * @param feeCollectionOrderPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(FeeCollectionOrderPo feeCollectionOrderPo) {

        int flag = feeCollectionOrderInnerServiceSMOImpl.updateFeeCollectionOrder(feeCollectionOrderPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
