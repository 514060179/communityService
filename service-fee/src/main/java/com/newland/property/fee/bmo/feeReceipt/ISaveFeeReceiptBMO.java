package com.newland.property.fee.bmo.feeReceipt;

import com.newland.property.po.fee.FeeReceiptPo;
import org.springframework.http.ResponseEntity;

public interface ISaveFeeReceiptBMO {


    /**
     * 添加收据
     * add by wuxw
     *
     * @param feeReceiptPo
     * @return
     */
    ResponseEntity<String> save(FeeReceiptPo feeReceiptPo);


}
