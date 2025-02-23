package com.newland.property.report.bmo.reportFeeMonthStatistics;
import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportFeeMonthStatisticsBMO {


    /**
     * 修改费用月统计
     * add by wuxw
     * @param reportFeeMonthStatisticsPo
     * @return
     */
    ResponseEntity<String> delete(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo);


}
