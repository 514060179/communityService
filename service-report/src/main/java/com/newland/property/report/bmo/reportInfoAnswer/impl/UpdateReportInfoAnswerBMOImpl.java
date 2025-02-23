package com.newland.property.report.bmo.reportInfoAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoAnswerPo;
import com.newland.property.report.bmo.reportInfoAnswer.IUpdateReportInfoAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateReportInfoAnswerBMOImpl")
public class UpdateReportInfoAnswerBMOImpl implements IUpdateReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportInfoAnswerPo reportInfoAnswerPo) {

        int flag = reportInfoAnswerInnerServiceSMOImpl.updateReportInfoAnswer(reportInfoAnswerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
