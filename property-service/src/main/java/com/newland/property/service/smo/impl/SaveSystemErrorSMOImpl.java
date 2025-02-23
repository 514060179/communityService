package com.newland.property.service.smo.impl;

import com.newland.property.intf.common.ILogSystemErrorInnerServiceSMO;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName SaveTransactionLogSMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/11/16 0:43
 * @Version 1.0
 * add by wuxw 2020/11/16
 **/
@Service
public class SaveSystemErrorSMOImpl implements ISaveSystemErrorSMO {

    @Autowired(required = false)
    private ILogSystemErrorInnerServiceSMO systemErrorInnerServiceSMOImpl;

    @Override
    @Async
    public void saveLog(LogSystemErrorPo logSystemErrorPo) {
        systemErrorInnerServiceSMOImpl.saveLogSystemError(logSystemErrorPo);
    }
}
