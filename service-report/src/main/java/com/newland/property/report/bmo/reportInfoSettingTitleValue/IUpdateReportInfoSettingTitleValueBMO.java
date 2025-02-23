package com.newland.property.report.bmo.reportInfoSettingTitleValue;
import com.newland.property.po.reportInfo.ReportInfoSettingTitleValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportInfoSettingTitleValueBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param reportInfoSettingTitleValuePo
     * @return
     */
    ResponseEntity<String> update(ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo);


}
