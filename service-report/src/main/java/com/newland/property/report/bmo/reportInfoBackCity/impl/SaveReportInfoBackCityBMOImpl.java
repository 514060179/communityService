package com.newland.property.report.bmo.reportInfoBackCity.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportInfoBackCityInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoBackCityPo;
import com.newland.property.report.bmo.reportInfoBackCity.ISaveReportInfoBackCityBMO;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportInfoBackCityBMOImpl")
public class SaveReportInfoBackCityBMOImpl implements ISaveReportInfoBackCityBMO {

    @Autowired
    private IReportInfoBackCityInnerServiceSMO reportInfoBackCityInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportInfoBackCityPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportInfoBackCityPo reportInfoBackCityPo) {
        if(StringUtil.isEmpty(reportInfoBackCityPo.getUserId())){
            reportInfoBackCityPo.setUserId("-1");
        }
        reportInfoBackCityPo.setBackId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_backId));
        int flag = reportInfoBackCityInnerServiceSMOImpl.saveReportInfoBackCity(reportInfoBackCityPo);
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
