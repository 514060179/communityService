package com.newland.property.report.bmo.reportFeeYearCollectionDetail;
import com.newland.property.po.reportFee.ReportFeeYearCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportFeeYearCollectionDetailBMO {


    /**
     * 修改费用年收费明细
     * add by wuxw
     * @param reportFeeYearCollectionDetailPo
     * @return
     */
    ResponseEntity<String> delete(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);


}
