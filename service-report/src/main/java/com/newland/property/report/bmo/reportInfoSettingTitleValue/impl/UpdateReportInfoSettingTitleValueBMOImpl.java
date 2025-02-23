package com.newland.property.report.bmo.reportInfoSettingTitleValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoSettingTitleValuePo;
import com.newland.property.report.bmo.reportInfoSettingTitleValue.IUpdateReportInfoSettingTitleValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateReportInfoSettingTitleValueBMOImpl")
public class UpdateReportInfoSettingTitleValueBMOImpl implements IUpdateReportInfoSettingTitleValueBMO {

    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoSettingTitleValuePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {

        int flag = reportInfoSettingTitleValueInnerServiceSMOImpl.updateReportInfoSettingTitleValue(reportInfoSettingTitleValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
