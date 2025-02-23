package com.newland.property.report.bmo.reportInfoAnswer.impl;

import com.newland.property.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.newland.property.report.bmo.reportInfoAnswer.IGetReportInfoAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.newland.property.dto.reportInfo.ReportInfoAnswerDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoAnswerBMOImpl")
public class GetReportInfoAnswerBMOImpl implements IGetReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     *
     *
     * @param  reportInfoAnswerDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ReportInfoAnswerDto reportInfoAnswerDto) {


        int count = reportInfoAnswerInnerServiceSMOImpl.queryReportInfoAnswersCount(reportInfoAnswerDto);

        List<ReportInfoAnswerDto> reportInfoAnswerDtos = null;
        if (count > 0) {
            reportInfoAnswerDtos = reportInfoAnswerInnerServiceSMOImpl.queryReportInfoAnswers(reportInfoAnswerDto);
        } else {
            reportInfoAnswerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoAnswerDto.getRow()), count, reportInfoAnswerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
