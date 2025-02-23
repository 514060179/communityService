package com.newland.property.report.bmo.reportInfoAnswer;
import com.newland.property.dto.reportInfo.ReportInfoAnswerDto;
import org.springframework.http.ResponseEntity;
public interface IGetReportInfoAnswerBMO {


    /**
     * 查询批量操作日志详情
     * add by wuxw
     * @param  reportInfoAnswerDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoAnswerDto reportInfoAnswerDto);


}
