package com.newland.property.fee.bmo.payFeeAudit;

import com.newland.property.po.payFee.PayFeeAuditPo;
import org.springframework.http.ResponseEntity;

public interface ISavePayFeeAuditBMO {


    /**
     * 添加缴费审核
     * add by wuxw
     *
     * @param payFeeAuditPo
     * @return
     */
    ResponseEntity<String> save(PayFeeAuditPo payFeeAuditPo);


}
