package com.newland.property.intf.user;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.po.owner.OwnerPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IOwnerInnerServiceSMO
 * @Description 业主接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/initializeOwnerApi")
public interface IInitializeOwnerInnerServiceSMO {

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param communityId 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteBuildingOwner", method = RequestMethod.POST)
    int deleteBuildingOwner(@RequestBody Map communityId);
}
