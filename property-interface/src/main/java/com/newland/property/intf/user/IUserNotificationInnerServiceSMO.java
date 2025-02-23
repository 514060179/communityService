package com.newland.property.intf.user;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.user.UserNotificationDto;
import com.newland.property.po.user.UserNotificationPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userNotificationApi")
public interface IUserNotificationInnerServiceSMO {

    @RequestMapping(value = "/saveUserNotification", method = RequestMethod.POST)
    int saveUserNotification(@RequestBody UserNotificationPo userNotificationPo);

    @RequestMapping(value = "/updateUserNotification", method = RequestMethod.POST)
    int updateUserNotification(@RequestBody UserNotificationPo userNotificationPo);

    @RequestMapping(value = "/deleteUserNotification", method = RequestMethod.POST)
    int deleteUserNotification(@RequestBody UserNotificationPo userNotificationPo);

    @RequestMapping(value = "/queryUserNotifications", method = RequestMethod.POST)
    List<UserNotificationDto> queryUserNotifications(@RequestBody UserNotificationDto userNotificationDto);

    @RequestMapping(value = "/queryUserNotificationsCount", method = RequestMethod.POST)
    int queryUserNotificationsCount(@RequestBody UserNotificationDto userNotificationDto);

    @RequestMapping(value = "/readUserNotification", method = RequestMethod.POST)
    int readUserNotification(@RequestBody UserNotificationDto userNotificationDto);

    @RequestMapping(value = "/readTypeNotification", method = RequestMethod.POST)
    int readTypeNotification(@RequestBody UserNotificationDto userNotificationDto);
}
