package com.newland.property.report.bmo.reportInfoBackCity.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportInfoBackCityInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoBackCityPo;
import com.newland.property.report.bmo.reportInfoBackCity.IUpdateReportInfoBackCityBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportInfoBackCityBMOImpl")
public class UpdateReportInfoBackCityBMOImpl implements IUpdateReportInfoBackCityBMO {

    @Autowired
    private IReportInfoBackCityInnerServiceSMO reportInfoBackCityInnerServiceSMOImpl;

    /**
     * @param reportInfoBackCityPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportInfoBackCityPo reportInfoBackCityPo) {

        int flag = reportInfoBackCityInnerServiceSMOImpl.updateReportInfoBackCity(reportInfoBackCityPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
