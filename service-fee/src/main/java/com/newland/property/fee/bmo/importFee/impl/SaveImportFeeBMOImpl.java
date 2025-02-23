package com.newland.property.fee.bmo.importFee.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.importFee.ISaveImportFeeBMO;
import com.newland.property.intf.fee.IImportFeeInnerServiceSMO;
import com.newland.property.po.importFee.ImportFeePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveImportFeeBMOImpl")
public class SaveImportFeeBMOImpl implements ISaveImportFeeBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param importFeePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ImportFeePo importFeePo) {

        importFeePo.setImportFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_feeId));
        int flag = importFeeInnerServiceSMOImpl.saveImportFee(importFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
