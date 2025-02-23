package com.newland.property.fee.bmo.importFee.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.importFee.IDeleteImportFeeBMO;
import com.newland.property.intf.fee.IImportFeeInnerServiceSMO;
import com.newland.property.po.importFee.ImportFeePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteImportFeeBMOImpl")
public class DeleteImportFeeBMOImpl implements IDeleteImportFeeBMO {

    @Autowired
    private IImportFeeInnerServiceSMO importFeeInnerServiceSMOImpl;

    /**
     * @param importFeePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ImportFeePo importFeePo) {

        int flag = importFeeInnerServiceSMOImpl.deleteImportFee(importFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
