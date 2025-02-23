package com.newland.property.fee.bmo.feeManualCollectionDetail;

import com.newland.property.po.fee.FeeManualCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeManualCollectionDetailBMO {


    /**
     * 修改托收明细
     * add by wuxw
     *
     * @param feeManualCollectionDetailPo
     * @return
     */
    ResponseEntity<String> update(FeeManualCollectionDetailPo feeManualCollectionDetailPo);


}
