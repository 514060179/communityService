package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.user.IOrgStaffRelInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.org.OrgStaffRelDto;
import com.newland.property.user.dao.IOrgStaffRelServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 组织员工关系内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OrgStaffRelInnerServiceSMOImpl extends BaseServiceSMO implements IOrgStaffRelInnerServiceSMO {

    @Autowired
    private IOrgStaffRelServiceDao orgStaffRelServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OrgStaffRelDto> queryOrgStaffRels(@RequestBody OrgStaffRelDto orgStaffRelDto) {

        //校验是否传了 分页信息

        int page = orgStaffRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            orgStaffRelDto.setPage((page - 1) * orgStaffRelDto.getRow());
        }

        List<OrgStaffRelDto> orgStaffRels = BeanConvertUtil.covertBeanList(orgStaffRelServiceDaoImpl.getOrgStaffRelInfo(BeanConvertUtil.beanCovertMap(orgStaffRelDto)), OrgStaffRelDto.class);

        return orgStaffRels;
    }


    @Override
    public int queryOrgStaffRelsCount(@RequestBody OrgStaffRelDto orgStaffRelDto) {
        return orgStaffRelServiceDaoImpl.queryOrgStaffRelsCount(BeanConvertUtil.beanCovertMap(orgStaffRelDto));
    }

    @Override
    public List<OrgStaffRelDto> queryOrgInfoByStaffIds(@RequestBody OrgStaffRelDto orgStaffRelDto) {
        List<OrgStaffRelDto> orgStaffRels = BeanConvertUtil.covertBeanList(orgStaffRelServiceDaoImpl.queryOrgInfoByStaffIds(BeanConvertUtil.beanCovertMap(orgStaffRelDto)), OrgStaffRelDto.class);
        return orgStaffRels;

    }

    @Override
    public List<OrgStaffRelDto> queryOrgInfoByStaffIdsNew(@RequestBody OrgStaffRelDto orgStaffRelDto) {
        List<OrgStaffRelDto> orgStaffRels = BeanConvertUtil.covertBeanList(orgStaffRelServiceDaoImpl.queryOrgInfoByStaffIdsNew(BeanConvertUtil.beanCovertMap(orgStaffRelDto)), OrgStaffRelDto.class);
        return orgStaffRels;

    }

    public IOrgStaffRelServiceDao getOrgStaffRelServiceDaoImpl() {
        return orgStaffRelServiceDaoImpl;
    }

    public void setOrgStaffRelServiceDaoImpl(IOrgStaffRelServiceDao orgStaffRelServiceDaoImpl) {
        this.orgStaffRelServiceDaoImpl = orgStaffRelServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
