package com.newland.property.report.bmo.reportFeeYearCollection;
import com.newland.property.dto.reportFee.ReportFeeYearCollectionDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportFeeYearCollectionBMO {


    /**
     * 查询费用年收费
     * add by wuxw
     * @param  reportFeeYearCollectionDto
     * @return
     */
    ResponseEntity<String> get(ReportFeeYearCollectionDto reportFeeYearCollectionDto);


}
