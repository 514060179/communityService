package com.newland.property.fee.bmo.feeManualCollection;

import com.newland.property.po.fee.FeeManualCollectionPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeManualCollectionBMO {


    /**
     * 修改人工托收
     * add by wuxw
     *
     * @param feeManualCollectionPo
     * @return
     */
    ResponseEntity<String> delete(FeeManualCollectionPo feeManualCollectionPo);


}
