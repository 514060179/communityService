package com.newland.property.report.bmo.reportInfoBackCity;
import com.newland.property.po.reportInfo.ReportInfoBackCityPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteReportInfoBackCityBMO {


    /**
     * 修改返省上报
     * add by wuxw
     * @param reportInfoBackCityPo
     * @return
     */
    ResponseEntity<String> delete(ReportInfoBackCityPo reportInfoBackCityPo);


}
