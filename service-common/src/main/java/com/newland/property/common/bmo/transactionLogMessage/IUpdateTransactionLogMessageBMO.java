package com.newland.property.common.bmo.transactionLogMessage;

import com.newland.property.po.log.TransactionLogMessagePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateTransactionLogMessageBMO {


    /**
     * 修改交互日志
     * add by wuxw
     *
     * @param transactionLogMessagePo
     * @return
     */
    ResponseEntity<String> update(TransactionLogMessagePo transactionLogMessagePo);


}
