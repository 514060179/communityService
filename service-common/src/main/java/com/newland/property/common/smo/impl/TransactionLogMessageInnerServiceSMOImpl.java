package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.ITransactionLogMessageServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.log.TransactionLogMessageDto;
import com.newland.property.intf.common.ITransactionLogMessageInnerServiceSMO;
import com.newland.property.po.log.TransactionLogMessagePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 交互日志内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class TransactionLogMessageInnerServiceSMOImpl extends BaseServiceSMO implements ITransactionLogMessageInnerServiceSMO {

    @Autowired
    private ITransactionLogMessageServiceDao transactionLogMessageServiceDaoImpl;


    @Override
    public int saveTransactionLogMessage(@RequestBody TransactionLogMessagePo transactionLogMessagePo) {
        int saveFlag = 1;
        transactionLogMessageServiceDaoImpl.saveTransactionLogMessageInfo(BeanConvertUtil.beanCovertMap(transactionLogMessagePo));
        return saveFlag;
    }

    @Override
    public int updateTransactionLogMessage(@RequestBody TransactionLogMessagePo transactionLogMessagePo) {
        int saveFlag = 1;
        transactionLogMessageServiceDaoImpl.updateTransactionLogMessageInfo(BeanConvertUtil.beanCovertMap(transactionLogMessagePo));
        return saveFlag;
    }

    @Override
    public int deleteTransactionLogMessage(@RequestBody TransactionLogMessagePo transactionLogMessagePo) {
        int saveFlag = 1;
        transactionLogMessageServiceDaoImpl.updateTransactionLogMessageInfo(BeanConvertUtil.beanCovertMap(transactionLogMessagePo));
        return saveFlag;
    }

    @Override
    public List<TransactionLogMessageDto> queryTransactionLogMessages(@RequestBody TransactionLogMessageDto transactionLogMessageDto) {

        //校验是否传了 分页信息

        int page = transactionLogMessageDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            transactionLogMessageDto.setPage((page - 1) * transactionLogMessageDto.getRow());
        }

        List<TransactionLogMessageDto> transactionLogMessages = BeanConvertUtil.covertBeanList(transactionLogMessageServiceDaoImpl.getTransactionLogMessageInfo(BeanConvertUtil.beanCovertMap(transactionLogMessageDto)), TransactionLogMessageDto.class);

        return transactionLogMessages;
    }


    @Override
    public int queryTransactionLogMessagesCount(@RequestBody TransactionLogMessageDto transactionLogMessageDto) {
        return transactionLogMessageServiceDaoImpl.queryTransactionLogMessagesCount(BeanConvertUtil.beanCovertMap(transactionLogMessageDto));
    }

    public ITransactionLogMessageServiceDao getTransactionLogMessageServiceDaoImpl() {
        return transactionLogMessageServiceDaoImpl;
    }

    public void setTransactionLogMessageServiceDaoImpl(ITransactionLogMessageServiceDao transactionLogMessageServiceDaoImpl) {
        this.transactionLogMessageServiceDaoImpl = transactionLogMessageServiceDaoImpl;
    }
}
