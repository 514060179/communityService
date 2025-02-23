package com.newland.property.report.bmo.reportInfoSettingTitle;
import com.newland.property.po.reportInfo.ReportInfoSettingTitlePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportInfoSettingTitleBMO {


    /**
     * 修改进出上报题目设置
     * add by wuxw
     * @param reportInfoSettingTitlePo
     * @return
     */
    ResponseEntity<String> update(ReportInfoSettingTitlePo reportInfoSettingTitlePo);


}
