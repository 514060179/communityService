package com.newland.property.intf.report;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/generatorFeeMonthStatisticsPrepaymentApi")
public interface IGeneratorFeeMonthStatisticsPrepaymentInnerServiceSMO {

    @RequestMapping(value = "/generatorPrepaymentData", method = RequestMethod.POST)
    public int generatorData(@RequestBody ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo);

}
