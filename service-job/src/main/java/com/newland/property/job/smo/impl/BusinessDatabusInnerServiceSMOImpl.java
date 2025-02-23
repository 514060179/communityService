package com.newland.property.job.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.system.BusinessDatabusDto;
import com.newland.property.intf.job.IBusinessDatabusInnerServiceSMO;
import com.newland.property.job.dao.IBusinessDatabusServiceDao;
import com.newland.property.po.business.BusinessDatabusPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业务数据同步内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class BusinessDatabusInnerServiceSMOImpl extends BaseServiceSMO implements IBusinessDatabusInnerServiceSMO {

    @Autowired
    private IBusinessDatabusServiceDao businessDatabusServiceDaoImpl;


    @Override
    public int saveBusinessDatabus(@RequestBody BusinessDatabusPo businessDatabusPo) {
        int saveFlag = 1;
        businessDatabusServiceDaoImpl.saveBusinessDatabusInfo(BeanConvertUtil.beanCovertMap(businessDatabusPo));
        return saveFlag;
    }

    @Override
    public int updateBusinessDatabus(@RequestBody BusinessDatabusPo businessDatabusPo) {
        int saveFlag = 1;
        businessDatabusServiceDaoImpl.updateBusinessDatabusInfo(BeanConvertUtil.beanCovertMap(businessDatabusPo));
        return saveFlag;
    }

    @Override
    public int deleteBusinessDatabus(@RequestBody BusinessDatabusPo businessDatabusPo) {
        int saveFlag = 1;
        businessDatabusPo.setStatusCd("1");
        businessDatabusServiceDaoImpl.updateBusinessDatabusInfo(BeanConvertUtil.beanCovertMap(businessDatabusPo));
        return saveFlag;
    }

    @Override
    public List<BusinessDatabusDto> queryBusinessDatabuss(@RequestBody BusinessDatabusDto businessDatabusDto) {

        //校验是否传了 分页信息

        int page = businessDatabusDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            businessDatabusDto.setPage((page - 1) * businessDatabusDto.getRow());
        }

        List<BusinessDatabusDto> businessDatabuss = BeanConvertUtil.covertBeanList(businessDatabusServiceDaoImpl.getBusinessDatabusInfo(BeanConvertUtil.beanCovertMap(businessDatabusDto)), BusinessDatabusDto.class);

        return businessDatabuss;
    }


    @Override
    public int queryBusinessDatabussCount(@RequestBody BusinessDatabusDto businessDatabusDto) {
        return businessDatabusServiceDaoImpl.queryBusinessDatabussCount(BeanConvertUtil.beanCovertMap(businessDatabusDto));
    }

    public IBusinessDatabusServiceDao getBusinessDatabusServiceDaoImpl() {
        return businessDatabusServiceDaoImpl;
    }

    public void setBusinessDatabusServiceDaoImpl(IBusinessDatabusServiceDao businessDatabusServiceDaoImpl) {
        this.businessDatabusServiceDaoImpl = businessDatabusServiceDaoImpl;
    }
}
