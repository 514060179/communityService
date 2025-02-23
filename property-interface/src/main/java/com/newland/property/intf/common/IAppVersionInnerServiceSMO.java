package com.newland.property.intf.common;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.app.AppVersionDto;
import com.newland.property.dto.area.AreaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 查询App版本信息
 */
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/appVersionApi")
public interface IAppVersionInnerServiceSMO {
    /**
     * <p>查询App版本</p>
     *
     * @return AppVersionDto 对象数据
     */
    @RequestMapping(value = "/getAppVersion", method = RequestMethod.POST)
    public List<AppVersionDto> getAppVersion(@RequestBody AppVersionDto appVersionDto);
}
