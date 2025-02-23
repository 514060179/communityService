package com.newland.property.fee.bmo.feeCollectionDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.feeCollectionDetail.ISaveFeeCollectionDetailBMO;
import com.newland.property.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.newland.property.po.fee.FeeCollectionDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveFeeCollectionDetailBMOImpl")
public class SaveFeeCollectionDetailBMOImpl implements ISaveFeeCollectionDetailBMO {

    @Autowired
    private IFeeCollectionDetailInnerServiceSMO feeCollectionDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeCollectionDetailPo feeCollectionDetailPo) {

        feeCollectionDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = feeCollectionDetailInnerServiceSMOImpl.saveFeeCollectionDetail(feeCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
