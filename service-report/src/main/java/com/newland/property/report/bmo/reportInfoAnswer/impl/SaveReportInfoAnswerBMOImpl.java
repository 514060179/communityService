package com.newland.property.report.bmo.reportInfoAnswer.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;

import com.newland.property.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoAnswerPo;
import com.newland.property.report.bmo.reportInfoAnswer.ISaveReportInfoAnswerBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportInfoAnswerBMOImpl")
public class SaveReportInfoAnswerBMOImpl implements ISaveReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportInfoAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportInfoAnswerPo reportInfoAnswerPo) {

        reportInfoAnswerPo.setUserAnId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userAnId));
        int flag = reportInfoAnswerInnerServiceSMOImpl.saveReportInfoAnswer(reportInfoAnswerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
