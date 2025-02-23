package com.newland.property.fee.bmo.importFeeDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.importFeeDetail.IUpdateImportFeeDetailBMO;
import com.newland.property.intf.fee.IImportFeeDetailInnerServiceSMO;
import com.newland.property.po.importFee.ImportFeeDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateImportFeeDetailBMOImpl")
public class UpdateImportFeeDetailBMOImpl implements IUpdateImportFeeDetailBMO {

    @Autowired
    private IImportFeeDetailInnerServiceSMO importFeeDetailInnerServiceSMOImpl;

    /**
     * @param importFeeDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ImportFeeDetailPo importFeeDetailPo) {

        int flag = importFeeDetailInnerServiceSMOImpl.updateImportFeeDetail(importFeeDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
