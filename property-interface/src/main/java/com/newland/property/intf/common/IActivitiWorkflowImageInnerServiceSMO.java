package com.newland.property.intf.common;

import com.newland.property.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/activitiWorkflowImageApi")
public interface IActivitiWorkflowImageInnerServiceSMO {

    /**
     * 启动流程
     *
     * @return
     */
    @RequestMapping(value = "/getWorkflowImage", method = RequestMethod.POST)
    public String getWorkflowImage(@RequestBody String taskId);



}
