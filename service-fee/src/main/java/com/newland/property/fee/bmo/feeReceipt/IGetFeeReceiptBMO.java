package com.newland.property.fee.bmo.feeReceipt;

import com.newland.property.dto.fee.FeeReceiptDto;
import com.newland.property.dto.fee.FeeReceiptDtoNew;
import org.springframework.http.ResponseEntity;

public interface IGetFeeReceiptBMO {


    /**
     * 查询收据
     * add by wuxw
     *
     * @param feeReceiptDto
     * @return
     */
    ResponseEntity<String> get(FeeReceiptDto feeReceiptDto);

    /**
     * 查询收据按照户查
     * add by wuxw
     *
     * @param feeReceiptDto
     * @return
     */
    ResponseEntity<String> gets(FeeReceiptDtoNew feeReceiptDto);


}
