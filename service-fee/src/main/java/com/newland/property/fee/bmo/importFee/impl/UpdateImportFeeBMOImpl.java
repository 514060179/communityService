package com.newland.property.fee.bmo.importFee.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.importFee.IUpdateImportFeeBMO;
import com.newland.property.intf.fee.IImportFeeInnerServiceSMO;
import com.newland.property.po.importFee.ImportFeePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateImportFeeBMOImpl")
public class UpdateImportFeeBMOImpl implements IUpdateImportFeeBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    /**
     * @param importFeePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ImportFeePo importFeePo) {

        int flag = importFeeInnerServiceSMOImpl.updateImportFee(importFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
