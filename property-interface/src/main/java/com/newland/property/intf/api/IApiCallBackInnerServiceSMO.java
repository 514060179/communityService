package com.newland.property.intf.api;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.po.advert.AdvertItemPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "api-service", configuration = {FeignConfiguration.class})
@RequestMapping("/apiCallBack")
public interface IApiCallBackInnerServiceSMO {

    @RequestMapping(value = "/webSentParkingArea", method = RequestMethod.POST)
    public int webSentParkingArea(@RequestBody JSONObject reqJson);
}
