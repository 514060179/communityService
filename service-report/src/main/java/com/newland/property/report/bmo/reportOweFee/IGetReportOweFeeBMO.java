package com.newland.property.report.bmo.reportOweFee;
import com.newland.property.dto.reportFee.ReportOweFeeDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportOweFeeBMO {


    /**
     * 查询欠费统计
     * add by wuxw
     * @param  reportOweFeeDto
     * @return
     */
    ResponseEntity<String> get(ReportOweFeeDto reportOweFeeDto);


    ResponseEntity<String> getAllFees(ReportOweFeeDto reportOweFeeDto);
}
