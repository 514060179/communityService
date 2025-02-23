package com.newland.property.fee.bmo.feeManualCollection;

import com.newland.property.po.fee.FeeManualCollectionPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeManualCollectionBMO {


    /**
     * 修改人工托收
     * add by wuxw
     *
     * @param feeManualCollectionPo
     * @return
     */
    ResponseEntity<String> update(FeeManualCollectionPo feeManualCollectionPo);


}
