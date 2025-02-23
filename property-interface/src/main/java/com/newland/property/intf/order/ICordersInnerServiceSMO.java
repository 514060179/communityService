package com.newland.property.intf.order;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.corder.CorderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(name = "order-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ordersApi")
public interface ICordersInnerServiceSMO {

    @RequestMapping(value = "/queryCordersCount", method = RequestMethod.POST)
    int queryCordersCount(@RequestBody CorderDto corderDto);


    @RequestMapping(value = "/queryCorders", method = RequestMethod.POST)
    List<CorderDto> queryCorders(@RequestBody CorderDto corderDto);

    @RequestMapping(value = "/queryUnitemLog", method = RequestMethod.POST)
    Map queryUnitemLog(@RequestBody Map info);
}
