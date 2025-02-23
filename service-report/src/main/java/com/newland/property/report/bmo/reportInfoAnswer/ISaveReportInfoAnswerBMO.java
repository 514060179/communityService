package com.newland.property.report.bmo.reportInfoAnswer;

import com.newland.property.po.reportInfo.ReportInfoAnswerPo;
import org.springframework.http.ResponseEntity;
public interface ISaveReportInfoAnswerBMO {


    /**
     * 添加批量操作日志详情
     * add by wuxw
     * @param reportInfoAnswerPo
     * @return
     */
    ResponseEntity<String> save(ReportInfoAnswerPo reportInfoAnswerPo);


}
