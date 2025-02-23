package com.newland.property.intf.store;


import com.newland.property.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 采购申请类
 */
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/purchase")
public interface IPurchaseApi {

}
