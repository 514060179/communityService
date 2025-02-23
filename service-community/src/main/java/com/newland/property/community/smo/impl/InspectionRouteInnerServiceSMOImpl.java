package com.newland.property.community.smo.impl;


import com.newland.property.community.dao.IInspectionRouteServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.community.IInspectionRouteInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.inspection.InspectionRouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 巡检路线内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class InspectionRouteInnerServiceSMOImpl extends BaseServiceSMO implements IInspectionRouteInnerServiceSMO {

    @Autowired
    private IInspectionRouteServiceDao inspectionRouteServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<InspectionRouteDto> queryInspectionRoutes(@RequestBody  InspectionRouteDto inspectionRouteDto) {

        //校验是否传了 分页信息

        int page = inspectionRouteDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            inspectionRouteDto.setPage((page - 1) * inspectionRouteDto.getRow());
        }

        List<InspectionRouteDto> inspectionRoutes = BeanConvertUtil.covertBeanList(inspectionRouteServiceDaoImpl.getInspectionRouteInfo(BeanConvertUtil.beanCovertMap(inspectionRouteDto)), InspectionRouteDto.class);

        if (inspectionRoutes == null || inspectionRoutes.size() == 0) {
            return inspectionRoutes;
        }

        return inspectionRoutes;
    }




    @Override
    public int queryInspectionRoutesCount(@RequestBody InspectionRouteDto inspectionRouteDto) {
        return inspectionRouteServiceDaoImpl.queryInspectionRoutesCount(BeanConvertUtil.beanCovertMap(inspectionRouteDto));    }

    public IInspectionRouteServiceDao getInspectionRouteServiceDaoImpl() {
        return inspectionRouteServiceDaoImpl;
    }

    public void setInspectionRouteServiceDaoImpl(IInspectionRouteServiceDao inspectionRouteServiceDaoImpl) {
        this.inspectionRouteServiceDaoImpl = inspectionRouteServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
