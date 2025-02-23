package com.newland.property.report.bmo.reportFeeMonthStatistics.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import com.newland.property.report.bmo.reportFeeMonthStatistics.ISaveReportFeeMonthStatisticsBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportFeeMonthStatisticsBMOImpl")
public class SaveReportFeeMonthStatisticsBMOImpl implements ISaveReportFeeMonthStatisticsBMO {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportFeeMonthStatisticsPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        int flag = reportFeeMonthStatisticsInnerServiceSMOImpl.saveReportFeeMonthStatistics(reportFeeMonthStatisticsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
