package com.newland.property.report.bmo.reportInfoSettingTitleValue.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoSettingTitleValuePo;
import com.newland.property.report.bmo.reportInfoSettingTitleValue.IDeleteReportInfoSettingTitleValueBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deleteReportInfoSettingTitleValueBMOImpl")
public class DeleteReportInfoSettingTitleValueBMOImpl implements IDeleteReportInfoSettingTitleValueBMO {

    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;

    /**
     * @param reportInfoSettingTitleValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {

        int flag = reportInfoSettingTitleValueInnerServiceSMOImpl.deleteReportInfoSettingTitleValue(reportInfoSettingTitleValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
