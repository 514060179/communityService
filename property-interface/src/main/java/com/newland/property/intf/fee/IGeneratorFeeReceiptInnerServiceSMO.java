package com.newland.property.intf.fee;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.fee.FeeReceiptDto;
import com.newland.property.dto.fee.FeeReceiptDtoNew;
import com.newland.property.po.fee.FeeReceiptPo;
import com.newland.property.po.fee.PayFeeDetailPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IFeeReceiptInnerServiceSMO
 * @Description 收据接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/generatorFeeReceiptApi")
public interface IGeneratorFeeReceiptInnerServiceSMO {


    @RequestMapping(value = "/generator", method = RequestMethod.POST)
   int generator(@RequestBody PayFeeDetailPo payFeeDetailPo);


}
