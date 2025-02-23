package com.newland.property.common.bmo.sysDocument.impl;

import com.newland.property.common.bmo.sysDocument.IDeleteSysDocumentBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.ISysDocumentInnerServiceSMO;
import com.newland.property.po.sysDocument.SysDocumentPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteSysDocumentBMOImpl")
public class DeleteSysDocumentBMOImpl implements IDeleteSysDocumentBMO {

    @Autowired
    private ISysDocumentInnerServiceSMO sysDocumentInnerServiceSMOImpl;

    /**
     * @param sysDocumentPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(SysDocumentPo sysDocumentPo) {

        int flag = sysDocumentInnerServiceSMOImpl.deleteSysDocument(sysDocumentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
