package com.newland.property.intf.fee;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName IPayFeeDetailMonthInnerServiceSMO
 * @Description 月缴费表接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/payFeeMonthApi")
public interface IPayFeeMonthInnerServiceSMO {


    /**
     * 缴费处理离散表
     * @param payFeeDetailRefreshFeeMonthDto
     * @return
     */
    @RequestMapping(value = "/payFeeDetailRefreshFeeMonth", method = RequestMethod.POST)
    int payFeeDetailRefreshFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);


    @RequestMapping(value = "/doGeneratorOrRefreshFeeMonth", method = RequestMethod.POST)
    int doGeneratorOrRefreshFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);

    @RequestMapping(value = "/doGeneratorOrRefreshAllFeeMonth", method = RequestMethod.POST)
    int doGeneratorOrRefreshAllFeeMonth(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);

    @RequestMapping(value = "/doGeneratorFeeMonths", method = RequestMethod.POST)
    void doGeneratorFeeMonths(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);

    @RequestMapping(value = "/doGeneratorOweFees", method = RequestMethod.POST)
    void doGeneratorOweFees(@RequestBody PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto);
}
