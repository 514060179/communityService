package com.newland.property.fee.bmo.feeReceiptDetail;
import com.newland.property.dto.fee.FeeReceiptDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetFeeReceiptDetailBMO {


    /**
     * 查询收据明细
     * add by wuxw
     * @param  feeReceiptDetailDto
     * @return
     */
    ResponseEntity<String> get(FeeReceiptDetailDto feeReceiptDetailDto);


}
