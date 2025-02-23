package com.newland.property.report.bmo.reportInfoAnswer;
import com.newland.property.po.reportInfo.ReportInfoAnswerPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportInfoAnswerBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param reportInfoAnswerPo
     * @return
     */
    ResponseEntity<String> update(ReportInfoAnswerPo reportInfoAnswerPo);


}
