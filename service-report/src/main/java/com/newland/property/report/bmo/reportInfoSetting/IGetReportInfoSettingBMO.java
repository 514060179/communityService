package com.newland.property.report.bmo.reportInfoSetting;

import com.newland.property.dto.reportInfo.ReportInfoSettingDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportInfoSettingBMO {

    /**
     * 查询进出上报
     * add by wuxw
     *
     * @param reportInfoSettingDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoSettingDto reportInfoSettingDto);

}
