package com.newland.property.fee.bmo.payFeeAudit;
import com.newland.property.po.payFee.PayFeeAuditPo;
import org.springframework.http.ResponseEntity;

public interface IDeletePayFeeAuditBMO {


    /**
     * 修改缴费审核
     * add by wuxw
     * @param payFeeAuditPo
     * @return
     */
    ResponseEntity<String> delete(PayFeeAuditPo payFeeAuditPo);


}
