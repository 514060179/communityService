package com.newland.property.fee.bmo.payFeeDetailMonth;
import com.newland.property.dto.payFee.PayFeeDetailMonthDto;
import org.springframework.http.ResponseEntity;
public interface IGetPayFeeDetailMonthBMO {


    /**
     * 查询月缴费表
     * add by wuxw
     * @param  payFeeDetailMonthDto
     * @return
     */
    ResponseEntity<String> get(PayFeeDetailMonthDto payFeeDetailMonthDto);


}
