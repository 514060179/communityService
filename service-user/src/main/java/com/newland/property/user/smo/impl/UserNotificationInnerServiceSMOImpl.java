package com.newland.property.user.smo.impl;

import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.user.UserNotificationDto;
import com.newland.property.intf.user.IUserNotificationInnerServiceSMO;
import com.newland.property.po.user.UserNotificationPo;
import com.newland.property.user.dao.IUserNotificationServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author Moonny
 */
@RestController
public class UserNotificationInnerServiceSMOImpl extends BaseServiceSMO implements IUserNotificationInnerServiceSMO {

    @Autowired
    private IUserNotificationServiceDao userNotificationServiceDaoImpl;

    @Override
    public int saveUserNotification(@RequestBody UserNotificationPo userNotificationPo) {
        int saveFlag = 1;
        userNotificationServiceDaoImpl.saveUserNotification(BeanConvertUtil.beanCovertMap(userNotificationPo));
        return saveFlag;
    }

    @Override
    public int updateUserNotification(@RequestBody UserNotificationPo userNotificationPo) {
        int saveFlag = 1;
        userNotificationServiceDaoImpl.updateUserNotification(BeanConvertUtil.beanCovertMap(userNotificationPo));
        return saveFlag;
    }

    @Override
    public int deleteUserNotification(@RequestBody UserNotificationPo userNotificationPo) {
        int saveFlag = 1;
        userNotificationServiceDaoImpl.deleteUserNotification(BeanConvertUtil.beanCovertMap(userNotificationPo));
        return saveFlag;
    }

    @Override
    public List<UserNotificationDto> queryUserNotifications(@RequestBody UserNotificationDto userNotificationDto) {
        //校验是否传了 分页信息
        int page = userNotificationDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userNotificationDto.setPage((page - 1) * userNotificationDto.getRow());
        }

        return BeanConvertUtil.covertBeanList(userNotificationServiceDaoImpl.queryUserNotifications(BeanConvertUtil.beanCovertMap(userNotificationDto)), UserNotificationDto.class);
    }

    @Override
    public int queryUserNotificationsCount(UserNotificationDto userNotificationDto) {
        return userNotificationServiceDaoImpl.queryUserNotificationsCount(BeanConvertUtil.beanCovertMap(userNotificationDto));
    }

    @Override
    public int readUserNotification(@RequestBody UserNotificationDto userNotificationDto) {
        int saveFlag = 1;
        userNotificationServiceDaoImpl.readUserNotification(BeanConvertUtil.beanCovertMap(userNotificationDto));
        return saveFlag;
    }

    @Override
    public int readTypeNotification(@RequestBody UserNotificationDto userNotificationDto) {
        int saveFlag = 1;
        userNotificationServiceDaoImpl.readTypeNotification(BeanConvertUtil.beanCovertMap(userNotificationDto));
        return saveFlag;
    }
}
