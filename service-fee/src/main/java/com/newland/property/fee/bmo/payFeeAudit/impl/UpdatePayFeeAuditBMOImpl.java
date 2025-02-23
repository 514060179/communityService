package com.newland.property.fee.bmo.payFeeAudit.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.payFeeAudit.IUpdatePayFeeAuditBMO;
import com.newland.property.intf.fee.IPayFeeAuditInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeAuditPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePayFeeAuditBMOImpl")
public class UpdatePayFeeAuditBMOImpl implements IUpdatePayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    /**
     * @param payFeeAuditPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(PayFeeAuditPo payFeeAuditPo) {

        int flag = payFeeAuditInnerServiceSMOImpl.updatePayFeeAudit(payFeeAuditPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
