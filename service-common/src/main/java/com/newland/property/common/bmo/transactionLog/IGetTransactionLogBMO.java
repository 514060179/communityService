package com.newland.property.common.bmo.transactionLog;

import com.newland.property.dto.log.TransactionLogDto;
import org.springframework.http.ResponseEntity;

public interface IGetTransactionLogBMO {


    /**
     * 查询交互日志
     * add by wuxw
     *
     * @param transactionLogDto
     * @return
     */
    ResponseEntity<String> get(TransactionLogDto transactionLogDto);


}
