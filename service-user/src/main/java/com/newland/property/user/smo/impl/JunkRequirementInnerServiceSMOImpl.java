package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.user.IJunkRequirementInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.itemRelease.JunkRequirementDto;
import com.newland.property.user.dao.IJunkRequirementServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 旧货市场内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class JunkRequirementInnerServiceSMOImpl extends BaseServiceSMO implements IJunkRequirementInnerServiceSMO {

    @Autowired
    private IJunkRequirementServiceDao junkRequirementServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<JunkRequirementDto> queryJunkRequirements(@RequestBody JunkRequirementDto junkRequirementDto) {

        //校验是否传了 分页信息

        int page = junkRequirementDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            junkRequirementDto.setPage((page - 1) * junkRequirementDto.getRow());
        }

        List<JunkRequirementDto> junkRequirements = BeanConvertUtil.covertBeanList(junkRequirementServiceDaoImpl.getJunkRequirementInfo(BeanConvertUtil.beanCovertMap(junkRequirementDto)), JunkRequirementDto.class);

        return junkRequirements;
    }


    @Override
    public int queryJunkRequirementsCount(@RequestBody JunkRequirementDto junkRequirementDto) {
        return junkRequirementServiceDaoImpl.queryJunkRequirementsCount(BeanConvertUtil.beanCovertMap(junkRequirementDto));
    }

    public IJunkRequirementServiceDao getJunkRequirementServiceDaoImpl() {
        return junkRequirementServiceDaoImpl;
    }

    public void setJunkRequirementServiceDaoImpl(IJunkRequirementServiceDao junkRequirementServiceDaoImpl) {
        this.junkRequirementServiceDaoImpl = junkRequirementServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
