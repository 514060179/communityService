package com.newland.property.report.bmo.reportInfoSettingTitleValue;
import com.newland.property.dto.reportInfo.ReportInfoSettingTitleValueDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportInfoSettingTitleValueBMO {


    /**
     * 查询批量操作日志详情
     * add by wuxw
     * @param  reportInfoSettingTitleValueDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto);
    /**
     * 查询批量操作日志详情
     * add by wuxw
     * @param  reportInfoSettingTitleValueDto
     * @return
     */
    ResponseEntity<String> getReportInfoSettingTitleValueInfoResult(ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto);


}
