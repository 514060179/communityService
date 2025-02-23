package com.newland.property.intf.report;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.newland.property.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.newland.property.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/queryPayFeeDetailInnerServiceSMOApi")
public interface IQueryPayFeeDetailInnerServiceSMO {

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    ResultVo query(@RequestBody ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto);

    @RequestMapping(value = "/queryPrepayment", method = RequestMethod.POST)
    ResultVo queryPrepayment(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    @RequestMapping(value = "/queryReportCollectFees", method = RequestMethod.POST)
    ResultVo queryReportCollectFees(@RequestBody ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);
}
