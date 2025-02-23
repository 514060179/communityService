package com.newland.property.common.bmo.transactionLog.impl;

import com.newland.property.common.bmo.transactionLog.IUpdateTransactionLogBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.ITransactionLogInnerServiceSMO;
import com.newland.property.po.log.TransactionLogPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateTransactionLogBMOImpl")
public class UpdateTransactionLogBMOImpl implements IUpdateTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * @param transactionLogPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(TransactionLogPo transactionLogPo) {

        int flag = transactionLogInnerServiceSMOImpl.updateTransactionLog(transactionLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
