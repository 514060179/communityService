package com.newland.property.report.bmo.reportFeeMonthStatistics;

import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import org.springframework.http.ResponseEntity;

public interface ISaveReportFeeMonthStatisticsBMO {


    /**
     * 添加费用月统计
     * add by wuxw
     *
     * @param reportFeeMonthStatisticsPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);


}
