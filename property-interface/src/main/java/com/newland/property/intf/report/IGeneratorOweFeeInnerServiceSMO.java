package com.newland.property.intf.report;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName IGeneratorFeeMonthStatisticsInnerServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/10/15 21:51
 * @Version 1.0
 * add by wuxw 2020/10/15
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/generatorOweFeeInnerServiceSMOApi")
public interface IGeneratorOweFeeInnerServiceSMO {



    @RequestMapping(value = "/generatorOweData", method = RequestMethod.POST)
    int generatorOweData(@RequestBody ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);

    /**
     * 计算单个费用欠费
     * @param feeDto
     * @return
     */
    @RequestMapping(value = "/computeOweFee", method = RequestMethod.POST)
    int computeOweFee(@RequestBody FeeDto feeDto);
}
