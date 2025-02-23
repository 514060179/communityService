package com.newland.property.common.bmo.transactionLogMessage.impl;

import com.newland.property.common.bmo.transactionLogMessage.ISaveTransactionLogMessageBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.ITransactionLogMessageInnerServiceSMO;
import com.newland.property.po.log.TransactionLogMessagePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveTransactionLogMessageBMOImpl")
public class SaveTransactionLogMessageBMOImpl implements ISaveTransactionLogMessageBMO {

    @Autowired
    private ITransactionLogMessageInnerServiceSMO transactionLogMessageInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param transactionLogMessagePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(TransactionLogMessagePo transactionLogMessagePo) {

        transactionLogMessagePo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        int flag = transactionLogMessageInnerServiceSMOImpl.saveTransactionLogMessage(transactionLogMessagePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
