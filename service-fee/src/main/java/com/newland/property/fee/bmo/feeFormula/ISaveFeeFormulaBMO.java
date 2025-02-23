package com.newland.property.fee.bmo.feeFormula;

import com.newland.property.po.fee.FeeFormulaPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeFormulaBMO {


    /**
     * 添加费用公式
     * add by wuxw
     *
     * @param feeFormulaPo
     * @return
     */
    ResponseEntity<String> save(FeeFormulaPo feeFormulaPo);


}
