package com.newland.property.fee.bmo.importFee;
import com.newland.property.po.importFee.ImportFeePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateImportFeeBMO {


    /**
     * 修改费用导入
     * add by wuxw
     * @param importFeePo
     * @return
     */
    ResponseEntity<String> update(ImportFeePo importFeePo);


}
