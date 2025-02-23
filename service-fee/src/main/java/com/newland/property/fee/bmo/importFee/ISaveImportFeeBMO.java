package com.newland.property.fee.bmo.importFee;

import com.newland.property.po.importFee.ImportFeePo;
import org.springframework.http.ResponseEntity;
public interface ISaveImportFeeBMO {


    /**
     * 添加费用导入
     * add by wuxw
     * @param importFeePo
     * @return
     */
    ResponseEntity<String> save(ImportFeePo importFeePo);


}
