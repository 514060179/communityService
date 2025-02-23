package com.newland.property.report.bmo.reportFeeYearCollection;
import com.newland.property.po.reportFee.ReportFeeYearCollectionPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportFeeYearCollectionBMO {


    /**
     * 修改费用年收费
     * add by wuxw
     * @param reportFeeYearCollectionPo
     * @return
     */
    ResponseEntity<String> delete(ReportFeeYearCollectionPo reportFeeYearCollectionPo);


}
