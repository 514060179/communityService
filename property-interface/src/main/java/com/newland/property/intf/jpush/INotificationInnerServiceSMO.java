package com.newland.property.intf.jpush;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.notification.NotificationTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/notificationApi")
public interface INotificationInnerServiceSMO {

    @RequestMapping(value = "/getNotificationTypes", method = RequestMethod.POST)
    List<NotificationTypeDto> getNotificationTypes();
}
