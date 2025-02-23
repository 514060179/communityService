package com.newland.property.fee.bmo.importFeeDetail;
import com.newland.property.po.importFee.ImportFeeDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateImportFeeDetailBMO {


    /**
     * 修改费用导入明细
     * add by wuxw
     * @param importFeeDetailPo
     * @return
     */
    ResponseEntity<String> update(ImportFeeDetailPo importFeeDetailPo);


}
