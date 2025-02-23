package com.newland.property.fee.bmo.importFeeDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.importFeeDetail.ISaveImportFeeDetailBMO;
import com.newland.property.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.newland.property.po.importFee.ImportFeeDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveImportFeeDetailBMOImpl")
public class SaveImportFeeDetailBMOImpl implements ISaveImportFeeDetailBMO {

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param importFeeDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ImportFeeDetailPo importFeeDetailPo) {

        importFeeDetailPo.setIfdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        int flag = importFeeDetailInnerServiceSMOImpl.saveImportFeeDetail(importFeeDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
