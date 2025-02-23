package com.newland.property.intf.user;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.owner.OwnerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerAppUserInnerServiceSMO
 * @Description 绑定业主接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerStatisticsApi")
public interface IOwnerStatisticsServiceBMO {

    /**
     * 查询业主统计信息
     * @param ownerDtos
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    List<OwnerDto> query(@RequestBody List<OwnerDto> ownerDtos);
}
