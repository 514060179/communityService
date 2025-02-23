package com.newland.property.boot.smo.pool;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.smo.ISaveTransactionLogSMO;
import com.newland.property.po.log.TransactionLogPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author fengtianying
 * @date 2018/5/14 11:54
 *
 */
@Component("logSaveDbThread")
@Scope("prototype")//spring 多例
public class LogSaveDbThread implements Runnable{

    protected Logger logger = LoggerFactory.getLogger(LogSaveDbThread.class);

    @Autowired
    private ISaveTransactionLogSMO saveTransactionLogSMOImpl;


    private TransactionLogPo transactionLogPo;


    public TransactionLogPo getTransactionLogPo() {
        return transactionLogPo;
    }

    public void setTransactionLogPo(TransactionLogPo transactionLogPo) {
        this.transactionLogPo = transactionLogPo;
    }

    @Override
    public void run() {
        logger.info("添加线程池记录  param={}", JSONObject.toJSONString(transactionLogPo));
        saveTransactionLogSMOImpl.saveLog(transactionLogPo);
    }
}
