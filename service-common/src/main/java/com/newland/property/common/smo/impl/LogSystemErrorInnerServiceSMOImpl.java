package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.ILogSystemErrorServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.intf.common.ILogSystemErrorInnerServiceSMO;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 系统异常内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class LogSystemErrorInnerServiceSMOImpl extends BaseServiceSMO implements ILogSystemErrorInnerServiceSMO {

    @Autowired
    private ILogSystemErrorServiceDao logSystemErrorServiceDaoImpl;


    @Override
    public int saveLogSystemError(@RequestBody LogSystemErrorPo logSystemErrorPo) {
        int saveFlag = 1;
        logSystemErrorServiceDaoImpl.saveLogSystemErrorInfo(BeanConvertUtil.beanCovertMap(logSystemErrorPo));
        return saveFlag;
    }

    @Override
    public int updateLogSystemError(@RequestBody LogSystemErrorPo logSystemErrorPo) {
        int saveFlag = 1;
        logSystemErrorServiceDaoImpl.updateLogSystemErrorInfo(BeanConvertUtil.beanCovertMap(logSystemErrorPo));
        return saveFlag;
    }

    @Override
    public int deleteLogSystemError(@RequestBody LogSystemErrorPo logSystemErrorPo) {
        int saveFlag = 1;
        logSystemErrorPo.setStatusCd("1");
        logSystemErrorServiceDaoImpl.updateLogSystemErrorInfo(BeanConvertUtil.beanCovertMap(logSystemErrorPo));
        return saveFlag;
    }

    @Override
    public List<LogSystemErrorDto> queryLogSystemErrors(@RequestBody LogSystemErrorDto logSystemErrorDto) {

        //校验是否传了 分页信息

        int page = logSystemErrorDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            logSystemErrorDto.setPage((page - 1) * logSystemErrorDto.getRow());
        }

        List<LogSystemErrorDto> logSystemErrors = BeanConvertUtil.covertBeanList(logSystemErrorServiceDaoImpl.getLogSystemErrorInfo(BeanConvertUtil.beanCovertMap(logSystemErrorDto)), LogSystemErrorDto.class);

        return logSystemErrors;
    }


    @Override
    public int queryLogSystemErrorsCount(@RequestBody LogSystemErrorDto logSystemErrorDto) {
        return logSystemErrorServiceDaoImpl.queryLogSystemErrorsCount(BeanConvertUtil.beanCovertMap(logSystemErrorDto));
    }

    public ILogSystemErrorServiceDao getLogSystemErrorServiceDaoImpl() {
        return logSystemErrorServiceDaoImpl;
    }

    public void setLogSystemErrorServiceDaoImpl(ILogSystemErrorServiceDao logSystemErrorServiceDaoImpl) {
        this.logSystemErrorServiceDaoImpl = logSystemErrorServiceDaoImpl;
    }
}
