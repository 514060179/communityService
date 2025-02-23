package com.newland.property.common.bmo.transactionLog.impl;

import com.newland.property.common.bmo.transactionLog.ISaveTransactionLogBMO;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.ITransactionLogInnerServiceSMO;
import com.newland.property.po.log.TransactionLogPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveTransactionLogBMOImpl")
public class SaveTransactionLogBMOImpl implements ISaveTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param transactionLogPo
     * @return 订单服务能够接受的报文
     */
    //@PropertyTransactional
    @Override
    public ResponseEntity<String> save(TransactionLogPo transactionLogPo) {

        transactionLogPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        int flag = transactionLogInnerServiceSMOImpl.saveTransactionLog(transactionLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
