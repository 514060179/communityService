package com.newland.property.report.bmo.reportFeeYearCollectionDetail;
import com.newland.property.po.reportFee.ReportFeeYearCollectionDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportFeeYearCollectionDetailBMO {


    /**
     * 修改费用年收费明细
     * add by wuxw
     * @param reportFeeYearCollectionDetailPo
     * @return
     */
    ResponseEntity<String> update(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo);


}
