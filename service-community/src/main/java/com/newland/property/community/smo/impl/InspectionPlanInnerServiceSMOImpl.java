package com.newland.property.community.smo.impl;


import com.newland.property.community.dao.IInspectionPlanServiceDao;
import com.newland.property.dto.inspection.InspectionPlanDto;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.community.IInspectionPlanInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检计划内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionPlanInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionPlanInnerServiceSMO {

    @Autowired
    private IInspectionPlanServiceDao inspectionPlanServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionPlanDto> queryInspectionPlans(@RequestBody  InspectionPlanDto inspectionPlanDto) {

        //校验是否传了 分页信息

        int page = inspectionPlanDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionPlanDto.setPage((page - 1) * inspectionPlanDto.getRow());
        }

        List<InspectionPlanDto> inspectionPlans = BeanConvertUtil.covertBeanList(inspectionPlanServiceDaoImpl.getInspectionPlanInfo(BeanConvertUtil.beanCovertMap(inspectionPlanDto)), InspectionPlanDto.class);

        return inspectionPlans;
    }




    @Override
    public int queryInspectionPlansCount(@RequestBody InspectionPlanDto inspectionPlanDto) {
        return inspectionPlanServiceDaoImpl.queryInspectionPlansCount(BeanConvertUtil.beanCovertMap(inspectionPlanDto));    }

    public IInspectionPlanServiceDao getInspectionPlanServiceDaoImpl() {
        return inspectionPlanServiceDaoImpl;
    }

    public void setInspectionPlanServiceDaoImpl(IInspectionPlanServiceDao inspectionPlanServiceDaoImpl) {
        this.inspectionPlanServiceDaoImpl = inspectionPlanServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
