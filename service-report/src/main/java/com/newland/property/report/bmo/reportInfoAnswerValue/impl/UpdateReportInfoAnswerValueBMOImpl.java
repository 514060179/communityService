package com.newland.property.report.bmo.reportInfoAnswerValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportInfoAnswerValueInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoAnswerValuePo;
import com.newland.property.report.bmo.reportInfoAnswerValue.IUpdateReportInfoAnswerValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportInfoAnswerValueBMOImpl")
public class UpdateReportInfoAnswerValueBMOImpl implements IUpdateReportInfoAnswerValueBMO {

    @Autowired
    private IReportInfoAnswerValueInnerServiceSMO reportInfoAnswerValueInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoAnswerValuePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportInfoAnswerValuePo reportInfoAnswerValuePo) {

        int flag = reportInfoAnswerValueInnerServiceSMOImpl.updateReportInfoAnswerValue(reportInfoAnswerValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
