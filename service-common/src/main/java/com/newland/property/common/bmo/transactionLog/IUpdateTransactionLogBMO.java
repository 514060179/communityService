package com.newland.property.common.bmo.transactionLog;
import com.newland.property.po.log.TransactionLogPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateTransactionLogBMO {


    /**
     * 修改交互日志
     * add by wuxw
     * @param transactionLogPo
     * @return
     */
    ResponseEntity<String> update(TransactionLogPo transactionLogPo);


}
