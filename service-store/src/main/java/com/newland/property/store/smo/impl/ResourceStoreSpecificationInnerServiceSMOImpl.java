package com.newland.property.store.smo.impl;


import com.newland.property.dto.resource.ResourceStoreSpecificationDto;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.store.dao.IResourceStoreSpecificationServiceDao;
import com.newland.property.intf.store.IResourceStoreSpecificationInnerServiceSMO;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 物品规格内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ResourceStoreSpecificationInnerServiceSMOImpl extends BaseServiceSMO implements IResourceStoreSpecificationInnerServiceSMO {

    @Autowired
    private IResourceStoreSpecificationServiceDao resourceStoreSpecificationServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<ResourceStoreSpecificationDto> queryResourceStoreSpecifications(@RequestBody ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecificationDto) {

        //校验是否传了 分页信息

        int page = resourceResourceStoreSpecificationSpecificationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            resourceResourceStoreSpecificationSpecificationDto.setPage((page - 1) * resourceResourceStoreSpecificationSpecificationDto.getRow());
        }

        List<ResourceStoreSpecificationDto> resourceResourceStoreSpecificationSpecifications = BeanConvertUtil.covertBeanList(resourceStoreSpecificationServiceDaoImpl.getResourceStoreSpecificationInfo(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationDto)), ResourceStoreSpecificationDto.class);

        if (resourceResourceStoreSpecificationSpecifications == null || resourceResourceStoreSpecificationSpecifications.size() == 0) {
            return resourceResourceStoreSpecificationSpecifications;
        }

        String[] userIds = getUserIds(resourceResourceStoreSpecificationSpecifications);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecification : resourceResourceStoreSpecificationSpecifications) {
            refreshResourceStoreSpecification(resourceResourceStoreSpecificationSpecification, users);
        }
        return resourceResourceStoreSpecificationSpecifications;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param resourceResourceStoreSpecificationSpecification 小区物品规格信息
     * @param users                                           用户列表
     */
    private void refreshResourceStoreSpecification(ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecification, List<UserDto> users) {
        for (UserDto user : users) {
            if (resourceResourceStoreSpecificationSpecification.getRssId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, resourceResourceStoreSpecificationSpecification);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param resourceResourceStoreSpecificationSpecifications 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<ResourceStoreSpecificationDto> resourceResourceStoreSpecificationSpecifications) {
        List<String> userIds = new ArrayList<String>();
        for (ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecification : resourceResourceStoreSpecificationSpecifications) {
            userIds.add(resourceResourceStoreSpecificationSpecification.getRssId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryResourceStoreSpecificationsCount(@RequestBody ResourceStoreSpecificationDto resourceResourceStoreSpecificationSpecificationDto) {
        return resourceStoreSpecificationServiceDaoImpl.queryResourceStoreSpecificationsCount(BeanConvertUtil.beanCovertMap(resourceResourceStoreSpecificationSpecificationDto));
    }

    public IResourceStoreSpecificationServiceDao getResourceStoreSpecificationServiceDaoImpl() {
        return resourceStoreSpecificationServiceDaoImpl;
    }

    public void setResourceStoreSpecificationServiceDaoImpl(IResourceStoreSpecificationServiceDao resourceStoreSpecificationServiceDaoImpl) {
        this.resourceStoreSpecificationServiceDaoImpl = resourceStoreSpecificationServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
