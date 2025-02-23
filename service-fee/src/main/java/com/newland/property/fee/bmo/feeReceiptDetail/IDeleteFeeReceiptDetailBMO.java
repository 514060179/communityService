package com.newland.property.fee.bmo.feeReceiptDetail;
import com.newland.property.po.fee.FeeReceiptDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteFeeReceiptDetailBMO {


    /**
     * 修改收据明细
     * add by wuxw
     * @param feeReceiptDetailPo
     * @return
     */
    ResponseEntity<String> delete(FeeReceiptDetailPo feeReceiptDetailPo);


}
