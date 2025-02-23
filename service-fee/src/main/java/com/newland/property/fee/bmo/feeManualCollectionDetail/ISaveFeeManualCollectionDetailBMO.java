package com.newland.property.fee.bmo.feeManualCollectionDetail;

import com.newland.property.po.fee.FeeManualCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeManualCollectionDetailBMO {


    /**
     * 添加托收明细
     * add by wuxw
     *
     * @param feeManualCollectionDetailPo
     * @return
     */
    ResponseEntity<String> save(FeeManualCollectionDetailPo feeManualCollectionDetailPo);


}
