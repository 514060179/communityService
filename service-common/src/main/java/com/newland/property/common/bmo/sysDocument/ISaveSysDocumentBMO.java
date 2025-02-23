package com.newland.property.common.bmo.sysDocument;

import com.newland.property.po.sysDocument.SysDocumentPo;
import org.springframework.http.ResponseEntity;
public interface ISaveSysDocumentBMO {


    /**
     * 添加系统文档
     * add by wuxw
     * @param sysDocumentPo
     * @return
     */
    ResponseEntity<String> save(SysDocumentPo sysDocumentPo);


}
