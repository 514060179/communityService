package com.newland.property.common.bmo.transactionLog;
import com.newland.property.po.log.TransactionLogPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteTransactionLogBMO {


    /**
     * 修改交互日志
     * add by wuxw
     * @param transactionLogPo
     * @return
     */
    ResponseEntity<String> delete(TransactionLogPo transactionLogPo);


}
