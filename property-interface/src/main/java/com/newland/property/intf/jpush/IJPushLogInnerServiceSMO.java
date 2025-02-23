package com.newland.property.intf.jpush;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.po.jpush.JPushLogPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/jpushLogApi")
public interface IJPushLogInnerServiceSMO {

    @RequestMapping(value = "/saveJPushLog", method = RequestMethod.POST)
    int saveJPushLog(@RequestBody JPushLogPo jPushLogPo);

    @RequestMapping(value = "/updateJPushLog", method = RequestMethod.POST)
    int updateJPushLog(@RequestBody JPushLogPo jPushLogPo);

    @RequestMapping(value = "/deleteJPushLog", method = RequestMethod.POST)
    int deleteJPushLog(@RequestBody JPushLogPo jPushLogPo);
}
