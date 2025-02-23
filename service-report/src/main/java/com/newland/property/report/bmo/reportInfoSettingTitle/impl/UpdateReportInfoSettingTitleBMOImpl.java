package com.newland.property.report.bmo.reportInfoSettingTitle.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoSettingTitlePo;
import com.newland.property.report.bmo.reportInfoSettingTitle.IUpdateReportInfoSettingTitleBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportInfoSettingTitleBMOImpl")
public class UpdateReportInfoSettingTitleBMOImpl implements IUpdateReportInfoSettingTitleBMO {

    @Autowired
    private IReportInfoSettingTitleInnerServiceSMO reportInfoSettingTitleInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoSettingTitlePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportInfoSettingTitlePo reportInfoSettingTitlePo) {

        int flag = reportInfoSettingTitleInnerServiceSMOImpl.updateReportInfoSettingTitle(reportInfoSettingTitlePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
