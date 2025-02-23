package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.user.StaffAppAuthDto;
import com.newland.property.intf.user.IStaffAppAuthInnerServiceSMO;
import com.newland.property.po.user.StaffAppAuthPo;
import com.newland.property.user.dao.IStaffAppAuthServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 员工微信认证内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class StaffAppAuthInnerServiceSMOImpl extends BaseServiceSMO implements IStaffAppAuthInnerServiceSMO {

    @Autowired
    private IStaffAppAuthServiceDao staffAppAuthServiceDaoImpl;


    @Override
    public int saveStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo) {
        int saveFlag = 1;
        staffAppAuthServiceDaoImpl.saveStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthPo));
        return saveFlag;
    }

    @Override
    public int updateStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo) {
        int saveFlag = 1;
        staffAppAuthServiceDaoImpl.updateStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthPo));
        return saveFlag;
    }

    @Override
    public int deleteStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo) {
        int saveFlag = 1;
        staffAppAuthPo.setStatusCd("1");
        staffAppAuthServiceDaoImpl.updateStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthPo));
        return saveFlag;
    }

    @Override
    public List<StaffAppAuthDto> queryStaffAppAuths(@RequestBody StaffAppAuthDto staffAppAuthDto) {

        //校验是否传了 分页信息

        int page = staffAppAuthDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            staffAppAuthDto.setPage((page - 1) * staffAppAuthDto.getRow());
        }

        List<StaffAppAuthDto> staffAppAuths = BeanConvertUtil.covertBeanList(staffAppAuthServiceDaoImpl.getStaffAppAuthInfo(BeanConvertUtil.beanCovertMap(staffAppAuthDto)), StaffAppAuthDto.class);

        return staffAppAuths;
    }


    @Override
    public int queryStaffAppAuthsCount(@RequestBody StaffAppAuthDto staffAppAuthDto) {
        return staffAppAuthServiceDaoImpl.queryStaffAppAuthsCount(BeanConvertUtil.beanCovertMap(staffAppAuthDto));
    }

    public IStaffAppAuthServiceDao getStaffAppAuthServiceDaoImpl() {
        return staffAppAuthServiceDaoImpl;
    }

    public void setStaffAppAuthServiceDaoImpl(IStaffAppAuthServiceDao staffAppAuthServiceDaoImpl) {
        this.staffAppAuthServiceDaoImpl = staffAppAuthServiceDaoImpl;
    }
}
