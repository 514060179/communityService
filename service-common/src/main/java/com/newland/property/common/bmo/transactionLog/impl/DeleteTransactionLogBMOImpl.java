package com.newland.property.common.bmo.transactionLog.impl;

import com.newland.property.common.bmo.transactionLog.IDeleteTransactionLogBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.ITransactionLogInnerServiceSMO;
import com.newland.property.po.log.TransactionLogPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteTransactionLogBMOImpl")
public class DeleteTransactionLogBMOImpl implements IDeleteTransactionLogBMO {

    @Autowired
    private ITransactionLogInnerServiceSMO transactionLogInnerServiceSMOImpl;

    /**
     * @param transactionLogPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(TransactionLogPo transactionLogPo) {

        int flag = transactionLogInnerServiceSMOImpl.deleteTransactionLog(transactionLogPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
