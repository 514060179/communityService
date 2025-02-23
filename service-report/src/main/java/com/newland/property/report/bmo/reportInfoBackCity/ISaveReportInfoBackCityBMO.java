package com.newland.property.report.bmo.reportInfoBackCity;

import com.newland.property.po.reportInfo.ReportInfoBackCityPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportInfoBackCityBMO {


    /**
     * 添加返省上报
     * add by wuxw
     * @param reportInfoBackCityPo
     * @return
     */
    ResponseEntity<String> save(ReportInfoBackCityPo reportInfoBackCityPo);


}
