package com.newland.property.common.bmo.transactionLogMessage.impl;

import com.newland.property.common.bmo.transactionLogMessage.IUpdateTransactionLogMessageBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.ITransactionLogMessageInnerServiceSMO;
import com.newland.property.po.log.TransactionLogMessagePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateTransactionLogMessageBMOImpl")
public class UpdateTransactionLogMessageBMOImpl implements IUpdateTransactionLogMessageBMO {

    @Autowired
    private ITransactionLogMessageInnerServiceSMO transactionLogMessageInnerServiceSMOImpl;

    /**
     * @param transactionLogMessagePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(TransactionLogMessagePo transactionLogMessagePo) {

        int flag = transactionLogMessageInnerServiceSMOImpl.updateTransactionLogMessage(transactionLogMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
