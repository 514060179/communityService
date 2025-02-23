package com.newland.property.fee.bmo.feeCollectionDetail;
import com.newland.property.po.fee.FeeCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeCollectionDetailBMO {


    /**
     * 修改催缴单
     * add by wuxw
     * @param feeCollectionDetailPo
     * @return
     */
    ResponseEntity<String> delete(FeeCollectionDetailPo feeCollectionDetailPo);


}
