package com.newland.property.report.bmo.reportFeeYearCollection;

import com.newland.property.po.reportFee.ReportFeeYearCollectionPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportFeeYearCollectionBMO {


    /**
     * 添加费用年收费
     * add by wuxw
     * @param reportFeeYearCollectionPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeYearCollectionPo reportFeeYearCollectionPo);


}
