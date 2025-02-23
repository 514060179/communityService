package com.newland.property.intf.user;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.user.UserAliasDto;
import com.newland.property.po.user.UserAliasPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 用户服务接口类（供服务间调用）
 */
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userAliasApi")
public interface IUserAliasInnerServiceSMO {

    @RequestMapping(value = "/saveUserAlias", method = RequestMethod.POST)
    int saveUserAlias(@RequestBody UserAliasPo userAliasPo);

    @RequestMapping(value = "/getUserAlias", method = RequestMethod.POST)
    List<UserAliasDto> getUserAlias(@RequestBody UserAliasDto userAliasDto);

    @RequestMapping(value = "/getUserAliasCount", method = RequestMethod.POST)
    int getUserAliasCount(@RequestBody UserAliasDto userAliasDto);

    @RequestMapping(value = "/updateUserAlias", method = RequestMethod.POST)
    int updateUserAlias(@RequestBody UserAliasDto UserAliasDto);
}
