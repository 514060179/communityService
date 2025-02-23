package com.newland.property.fee.bmo.feePrintSpec;

import com.newland.property.po.fee.feePrintSpec.FeePrintSpecPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeePrintSpecBMO {


    /**
     * 修改打印说明
     * add by wuxw
     *
     * @param feePrintSpecPo
     * @return
     */
    ResponseEntity<String> delete(FeePrintSpecPo feePrintSpecPo);


}
