package com.newland.property.report.bmo.reportInfoAnswerValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;

import com.newland.property.intf.report.IReportInfoAnswerValueInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoAnswerValuePo;
import com.newland.property.report.bmo.reportInfoAnswerValue.IDeleteReportInfoAnswerValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deleteReportInfoAnswerValueBMOImpl")
public class DeleteReportInfoAnswerValueBMOImpl implements IDeleteReportInfoAnswerValueBMO {

    @Autowired
    private IReportInfoAnswerValueInnerServiceSMO reportInfoAnswerValueInnerServiceSMOImpl;

    /**
     * @param reportInfoAnswerValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ReportInfoAnswerValuePo reportInfoAnswerValuePo) {

        int flag = reportInfoAnswerValueInnerServiceSMOImpl.deleteReportInfoAnswerValue(reportInfoAnswerValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
