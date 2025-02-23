package com.newland.property.report.bmo.reportInfoAnswerValue;
import com.newland.property.po.reportInfo.ReportInfoAnswerValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportInfoAnswerValueBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param reportInfoAnswerValuePo
     * @return
     */
    ResponseEntity<String> update(ReportInfoAnswerValuePo reportInfoAnswerValuePo);


}
