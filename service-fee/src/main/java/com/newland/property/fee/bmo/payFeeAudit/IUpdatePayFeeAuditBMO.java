package com.newland.property.fee.bmo.payFeeAudit;
import com.newland.property.po.payFee.PayFeeAuditPo;
import org.springframework.http.ResponseEntity;

public interface IUpdatePayFeeAuditBMO {


    /**
     * 修改缴费审核
     * add by wuxw
     * @param payFeeAuditPo
     * @return
     */
    ResponseEntity<String> update(PayFeeAuditPo payFeeAuditPo);


}
