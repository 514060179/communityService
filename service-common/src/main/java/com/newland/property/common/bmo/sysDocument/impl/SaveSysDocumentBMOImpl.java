package com.newland.property.common.bmo.sysDocument.impl;

import com.newland.property.common.bmo.sysDocument.ISaveSysDocumentBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.ISysDocumentInnerServiceSMO;
import com.newland.property.po.sysDocument.SysDocumentPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveSysDocumentBMOImpl")
public class SaveSysDocumentBMOImpl implements ISaveSysDocumentBMO {

    @Autowired
    private ISysDocumentInnerServiceSMO sysDocumentInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param sysDocumentPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(SysDocumentPo sysDocumentPo) {

        sysDocumentPo.setDocId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_docId));
        int flag = sysDocumentInnerServiceSMOImpl.saveSysDocument(sysDocumentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
