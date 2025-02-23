package com.newland.property.fee.bmo.feeCollectionDetail;

import com.newland.property.po.fee.FeeCollectionDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveFeeCollectionDetailBMO {


    /**
     * 添加催缴单
     * add by wuxw
     * @param feeCollectionDetailPo
     * @return
     */
    ResponseEntity<String> save(FeeCollectionDetailPo feeCollectionDetailPo);


}
