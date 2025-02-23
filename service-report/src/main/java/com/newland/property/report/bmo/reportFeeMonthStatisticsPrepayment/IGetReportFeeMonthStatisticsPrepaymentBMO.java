package com.newland.property.report.bmo.reportFeeMonthStatisticsPrepayment;

import com.newland.property.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportFeeMonthStatisticsPrepaymentBMO {

    ResponseEntity<String> queryPayFeeDetail(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

    ResponseEntity<String> queryReportCollectFees(ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto);

}
