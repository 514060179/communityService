package com.newland.property.fee.bmo.feeReceipt;
import com.newland.property.po.fee.FeeReceiptPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateFeeReceiptBMO {


    /**
     * 修改收据
     * add by wuxw
     * @param feeReceiptPo
     * @return
     */
    ResponseEntity<String> update(FeeReceiptPo feeReceiptPo);


}
