package com.newland.property.fee.bmo.payFeeAudit;

import com.newland.property.dto.payFee.PayFeeAuditDto;
import org.springframework.http.ResponseEntity;

public interface IGetPayFeeAuditBMO {


    /**
     * 查询缴费审核
     * add by wuxw
     *
     * @param payFeeAuditDto
     * @return
     */
    ResponseEntity<String> get(PayFeeAuditDto payFeeAuditDto);


}
