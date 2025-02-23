package com.newland.property.report.bmo.reportInfoSettingTitle;
import com.newland.property.dto.reportInfo.ReportInfoSettingTitleDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportInfoSettingTitleBMO {


    /**
     * 查询进出上报题目设置
     * add by wuxw
     * @param  reportInfoSettingTitleDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoSettingTitleDto reportInfoSettingTitleDto);


}
