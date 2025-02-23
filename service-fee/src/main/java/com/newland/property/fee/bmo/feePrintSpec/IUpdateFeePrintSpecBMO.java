package com.newland.property.fee.bmo.feePrintSpec;

import com.newland.property.po.fee.feePrintSpec.FeePrintSpecPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeePrintSpecBMO {


    /**
     * 修改打印说明
     * add by wuxw
     *
     * @param feePrintSpecPo
     * @return
     */
    ResponseEntity<String> update(FeePrintSpecPo feePrintSpecPo);


}
