package com.newland.property.common.smo.impl;

import com.newland.property.common.dao.INotificationTypeServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.notification.NotificationTypeDto;
import com.newland.property.intf.jpush.INotificationInnerServiceSMO;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moonny
 */
@RestController
public class NotificationTypeInnerServiceSMOImpl  extends BaseServiceSMO implements INotificationInnerServiceSMO {

    @Autowired
    private INotificationTypeServiceDao notificationTypeServiceDaoImpl;

    @Override
    public List<NotificationTypeDto> getNotificationTypes() {
        Map info = new HashMap();
        return BeanConvertUtil.covertBeanList(notificationTypeServiceDaoImpl.getNotificationTypes(info),NotificationTypeDto.class);
    }
}
