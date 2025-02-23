package com.newland.property.fee.bmo.importFee;

import com.newland.property.po.importFee.ImportFeePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteImportFeeBMO {


    /**
     * 修改费用导入
     * add by wuxw
     *
     * @param importFeePo
     * @return
     */
    ResponseEntity<String> delete(ImportFeePo importFeePo);


}
