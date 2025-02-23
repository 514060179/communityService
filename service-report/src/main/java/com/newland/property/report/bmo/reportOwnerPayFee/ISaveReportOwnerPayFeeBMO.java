package com.newland.property.report.bmo.reportOwnerPayFee;

import com.newland.property.po.reportFee.ReportOwnerPayFeePo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportOwnerPayFeeBMO {


    /**
     * 添加业主缴费明细
     * add by wuxw
     * @param reportOwnerPayFeePo
     * @return
     */
    ResponseEntity<String> save(ReportOwnerPayFeePo reportOwnerPayFeePo);


}
