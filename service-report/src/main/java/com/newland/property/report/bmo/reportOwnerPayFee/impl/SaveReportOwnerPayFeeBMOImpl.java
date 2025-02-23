package com.newland.property.report.bmo.reportOwnerPayFee.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.newland.property.po.reportFee.ReportOwnerPayFeePo;
import com.newland.property.report.bmo.reportOwnerPayFee.ISaveReportOwnerPayFeeBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportOwnerPayFeeBMOImpl")
public class SaveReportOwnerPayFeeBMOImpl implements ISaveReportOwnerPayFeeBMO {

    @Autowired
    private IReportOwnerPayFeeInnerServiceSMO reportOwnerPayFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportOwnerPayFeePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportOwnerPayFeePo reportOwnerPayFeePo) {

        reportOwnerPayFeePo.setPfId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_pfId));
        int flag = reportOwnerPayFeeInnerServiceSMOImpl.saveReportOwnerPayFee(reportOwnerPayFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
