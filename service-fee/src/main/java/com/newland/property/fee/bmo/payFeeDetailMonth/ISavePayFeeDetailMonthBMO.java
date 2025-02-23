package com.newland.property.fee.bmo.payFeeDetailMonth;

import com.newland.property.po.payFee.PayFeeDetailMonthPo;
import org.springframework.http.ResponseEntity;
public interface ISavePayFeeDetailMonthBMO {


    /**
     * 添加月缴费表
     * add by wuxw
     * @param payFeeDetailMonthPo
     * @return
     */
    ResponseEntity<String> save(PayFeeDetailMonthPo payFeeDetailMonthPo);


}
