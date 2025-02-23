package com.newland.property.report.bmo.reportFeeMonthStatistics.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import com.newland.property.report.bmo.reportFeeMonthStatistics.IDeleteReportFeeMonthStatisticsBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportFeeMonthStatisticsBMOImpl")
public class DeleteReportFeeMonthStatisticsBMOImpl implements IDeleteReportFeeMonthStatisticsBMO {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    /**
     * @param reportFeeMonthStatisticsPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        int flag = reportFeeMonthStatisticsInnerServiceSMOImpl.deleteReportFeeMonthStatistics(reportFeeMonthStatisticsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
