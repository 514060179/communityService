package com.newland.property.common.bmo.transactionLogMessage;

import com.newland.property.dto.log.TransactionLogMessageDto;
import org.springframework.http.ResponseEntity;

public interface IGetTransactionLogMessageBMO {


    /**
     * 查询交互日志
     * add by wuxw
     *
     * @param transactionLogMessageDto
     * @return
     */
    ResponseEntity<String> get(TransactionLogMessageDto transactionLogMessageDto);


}
