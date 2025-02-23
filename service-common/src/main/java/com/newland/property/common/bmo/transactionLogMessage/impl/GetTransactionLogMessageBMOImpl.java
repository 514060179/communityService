package com.newland.property.common.bmo.transactionLogMessage.impl;

import com.newland.property.common.bmo.transactionLogMessage.IGetTransactionLogMessageBMO;
import com.newland.property.dto.log.TransactionLogMessageDto;
import com.newland.property.intf.common.ITransactionLogMessageInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getTransactionLogMessageBMOImpl")
public class GetTransactionLogMessageBMOImpl implements IGetTransactionLogMessageBMO {

    @Autowired
    private ITransactionLogMessageInnerServiceSMO transactionLogMessageInnerServiceSMOImpl;

    /**
     * @param transactionLogMessageDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(TransactionLogMessageDto transactionLogMessageDto) {


        int count = transactionLogMessageInnerServiceSMOImpl.queryTransactionLogMessagesCount(transactionLogMessageDto);

        List<TransactionLogMessageDto> transactionLogMessageDtos = null;
        if (count > 0) {
            transactionLogMessageDtos = transactionLogMessageInnerServiceSMOImpl.queryTransactionLogMessages(transactionLogMessageDto);
        } else {
            transactionLogMessageDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) transactionLogMessageDto.getRow()), count, transactionLogMessageDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
