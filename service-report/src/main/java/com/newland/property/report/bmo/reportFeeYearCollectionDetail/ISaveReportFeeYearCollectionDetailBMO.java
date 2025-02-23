package com.newland.property.report.bmo.reportFeeYearCollectionDetail;

import com.newland.property.po.reportFee.ReportFeeYearCollectionDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportFeeYearCollectionDetailBMO {


    /**
     * 添加费用年收费明细
     * add by wuxw
     * @param reportFeeYearCollectionDetailPo
     * @return
     */
    ResponseEntity<String> save(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);


}
