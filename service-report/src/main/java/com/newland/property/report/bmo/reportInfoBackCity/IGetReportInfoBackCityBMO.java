package com.newland.property.report.bmo.reportInfoBackCity;

import com.newland.property.dto.reportInfo.ReportInfoBackCityDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportInfoBackCityBMO {

    /**
     * 查询返省上报
     * add by wuxw
     *
     * @param reportInfoBackCityDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoBackCityDto reportInfoBackCityDto);

}
