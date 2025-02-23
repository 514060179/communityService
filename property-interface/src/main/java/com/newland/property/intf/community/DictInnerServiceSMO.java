package com.newland.property.intf.community;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.dict.DictDto;
import com.newland.property.dto.dict.DictQueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <br>
 * Created by hu ping on 10/22/2019
 * <p>
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/dictApi")
public interface DictInnerServiceSMO {

    @RequestMapping(value = "/queryDict",method = RequestMethod.POST)
    List<DictDto> queryDict(@RequestBody DictQueryDto queryDto);

}
