package com.newland.property.report.bmo.reportInfoSetting.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportInfoSettingInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoSettingPo;
import com.newland.property.report.bmo.reportInfoSetting.ISaveReportInfoSettingBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportInfoSettingBMOImpl")
public class SaveReportInfoSettingBMOImpl implements ISaveReportInfoSettingBMO {

    @Autowired
    private IReportInfoSettingInnerServiceSMO reportInfoSettingInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportInfoSettingPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportInfoSettingPo reportInfoSettingPo) {

        reportInfoSettingPo.setSettingId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_settingId));
        int flag = reportInfoSettingInnerServiceSMOImpl.saveReportInfoSetting(reportInfoSettingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
