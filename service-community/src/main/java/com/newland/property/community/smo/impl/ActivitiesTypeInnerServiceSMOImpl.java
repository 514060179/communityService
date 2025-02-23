package com.newland.property.community.smo.impl;


import com.newland.property.community.dao.IActivitiesTypeServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.activities.ActivitiesTypeDto;
import com.newland.property.intf.community.IActivitiesTypeInnerServiceSMO;
import com.newland.property.po.activities.ActivitiesTypePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 信息分类内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ActivitiesTypeInnerServiceSMOImpl extends BaseServiceSMO implements IActivitiesTypeInnerServiceSMO {

    @Autowired
    private IActivitiesTypeServiceDao activitiesTypeServiceDaoImpl;


    @Override
    public int saveActivitiesType(@RequestBody ActivitiesTypePo activitiesTypePo) {
        int saveFlag = 1;
        activitiesTypeServiceDaoImpl.saveActivitiesTypeInfo(BeanConvertUtil.beanCovertMap(activitiesTypePo));
        return saveFlag;
    }

    @Override
    public int updateActivitiesType(@RequestBody ActivitiesTypePo activitiesTypePo) {
        int saveFlag = 1;
        activitiesTypeServiceDaoImpl.updateActivitiesTypeInfo(BeanConvertUtil.beanCovertMap(activitiesTypePo));
        return saveFlag;
    }

    @Override
    public int deleteActivitiesType(@RequestBody ActivitiesTypePo activitiesTypePo) {
        int saveFlag = 1;
        activitiesTypePo.setStatusCd("1");
        activitiesTypeServiceDaoImpl.updateActivitiesTypeInfo(BeanConvertUtil.beanCovertMap(activitiesTypePo));
        return saveFlag;
    }

    @Override
    public List<ActivitiesTypeDto> queryActivitiesTypes(@RequestBody ActivitiesTypeDto activitiesTypeDto) {

        //校验是否传了 分页信息

        int page = activitiesTypeDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            activitiesTypeDto.setPage((page - 1) * activitiesTypeDto.getRow());
        }

        List<ActivitiesTypeDto> activitiesTypes = BeanConvertUtil.covertBeanList(activitiesTypeServiceDaoImpl.getActivitiesTypeInfo(BeanConvertUtil.beanCovertMap(activitiesTypeDto)), ActivitiesTypeDto.class);

        return activitiesTypes;
    }


    @Override
    public int queryActivitiesTypesCount(@RequestBody ActivitiesTypeDto activitiesTypeDto) {
        return activitiesTypeServiceDaoImpl.queryActivitiesTypesCount(BeanConvertUtil.beanCovertMap(activitiesTypeDto));
    }

    public IActivitiesTypeServiceDao getActivitiesTypeServiceDaoImpl() {
        return activitiesTypeServiceDaoImpl;
    }

    public void setActivitiesTypeServiceDaoImpl(IActivitiesTypeServiceDao activitiesTypeServiceDaoImpl) {
        this.activitiesTypeServiceDaoImpl = activitiesTypeServiceDaoImpl;
    }
}
