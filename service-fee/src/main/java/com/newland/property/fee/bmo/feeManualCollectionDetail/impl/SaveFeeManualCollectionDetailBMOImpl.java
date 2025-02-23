package com.newland.property.fee.bmo.feeManualCollectionDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeManualCollectionDetail.ISaveFeeManualCollectionDetailBMO;
import com.newland.property.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeManualCollectionDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeManualCollectionDetailBMOImpl")
public class SaveFeeManualCollectionDetailBMOImpl implements ISaveFeeManualCollectionDetailBMO {

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeManualCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeManualCollectionDetailPo feeManualCollectionDetailPo) {

        feeManualCollectionDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = feeManualCollectionDetailInnerServiceSMOImpl.saveFeeManualCollectionDetail(feeManualCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
