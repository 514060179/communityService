package com.newland.property.user.smo.impl;

import com.newland.property.dto.PageDto;
import com.newland.property.dto.user.UserAliasDto;
import com.newland.property.intf.user.IUserAliasInnerServiceSMO;
import com.newland.property.po.user.UserAliasPo;
import com.newland.property.user.dao.IUserAliasServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Moonny
 */
@RestController
public class UserAliasInnerServiceSMOImpl implements IUserAliasInnerServiceSMO {

    @Autowired
    private IUserAliasServiceDao userAliasServiceDaoImpl;

    @Override
    public int saveUserAlias(UserAliasPo userAliasPo) {
        return userAliasServiceDaoImpl.saveUserAlias(BeanConvertUtil.beanCovertMap(userAliasPo));
    }

    @Override
    public int updateUserAlias(UserAliasDto userAliasDto) {
        return userAliasServiceDaoImpl.updateUserAlias(BeanConvertUtil.beanCovertMap(userAliasDto));
    }

    @Override
    public List<UserAliasDto> getUserAlias(UserAliasDto userAliasDto) {
        //校验是否传了 分页信息

        int page = userAliasDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userAliasDto.setPage((page - 1) * userAliasDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(userAliasServiceDaoImpl.getUserAlias(BeanConvertUtil.beanCovertMap(userAliasDto)), UserAliasDto.class);
    }

    @Override
    public int getUserAliasCount(UserAliasDto userAliasDto) {
        return userAliasServiceDaoImpl.queryUserAliasCount(BeanConvertUtil.beanCovertMap(userAliasDto));
    }
}
