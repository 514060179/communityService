package com.newland.property.fee.bmo.feePrintSpec;

import com.newland.property.po.fee.feePrintSpec.FeePrintSpecPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeePrintSpecBMO {


    /**
     * 添加打印说明
     * add by wuxw
     *
     * @param feePrintSpecPo
     * @return
     */
    ResponseEntity<String> save(FeePrintSpecPo feePrintSpecPo);


}
