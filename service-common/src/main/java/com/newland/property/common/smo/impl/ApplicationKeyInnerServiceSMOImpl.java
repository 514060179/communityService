package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.IApplicationKeyServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.common.IApplicationKeyInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.machine.ApplicationKeyDto;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 钥匙申请内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ApplicationKeyInnerServiceSMOImpl extends BaseServiceSMO implements IApplicationKeyInnerServiceSMO {

    @Autowired
    private IApplicationKeyServiceDao applicationKeyServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ApplicationKeyDto> queryApplicationKeys(@RequestBody ApplicationKeyDto applicationKeyDto) {

        //校验是否传了 分页信息

        int page = applicationKeyDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            applicationKeyDto.setPage((page - 1) * applicationKeyDto.getRow());
        }

        List<ApplicationKeyDto> applicationKeys = BeanConvertUtil.covertBeanList(applicationKeyServiceDaoImpl.getApplicationKeyInfo(BeanConvertUtil.beanCovertMap(applicationKeyDto)), ApplicationKeyDto.class);

        return applicationKeys;
    }


    @Override
    public int queryApplicationKeysCount(@RequestBody ApplicationKeyDto applicationKeyDto) {
        return applicationKeyServiceDaoImpl.queryApplicationKeysCount(BeanConvertUtil.beanCovertMap(applicationKeyDto));
    }

    public IApplicationKeyServiceDao getApplicationKeyServiceDaoImpl() {
        return applicationKeyServiceDaoImpl;
    }

    public void setApplicationKeyServiceDaoImpl(IApplicationKeyServiceDao applicationKeyServiceDaoImpl) {
        this.applicationKeyServiceDaoImpl = applicationKeyServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
