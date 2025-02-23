package com.newland.property.report.cmd.reportFeeMonthStatistics;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.reportFee.ReportFeeMonthStatisticsDto;
import com.newland.property.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 楼栋费用表明细
 */
@NewlandPropertyCmd(serviceCode = "/reportFeeMonthStatistics/queryReportFloorUnitFeeSummaryDetail")
public class QueryReportFloorUnitFeeSummaryDetailCmd extends Cmd {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未传入小区信息");
        Assert.hasKeyAndValue(reqJson, "feeYear", "未传入年份");
        Assert.hasKeyAndValue(reqJson, "feeMonth", "未传入月份");
        Assert.hasKeyAndValue(reqJson, "floorNum", "未传入楼栋");
        Assert.hasKeyAndValue(reqJson, "unitNum", "未传入单元");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String[] configIds = null;
        if (reqJson.containsKey("configIds") && !StringUtil.isEmpty(reqJson.getString("configIds"))) {
            configIds = reqJson.getString("configIds").split(",");
            reqJson.remove("configIds");
        }
        ReportFeeMonthStatisticsDto reportFeeMonthStatisticsDto = BeanConvertUtil.covertBean(reqJson, ReportFeeMonthStatisticsDto.class);
        reportFeeMonthStatisticsDto.setConfigIds(configIds);

        int count = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeSummaryDetailCount(reportFeeMonthStatisticsDto);
        List<ReportFeeMonthStatisticsDto> reportFeeMonthStatisticsDtos = null;
        if (count > 0) {
            reportFeeMonthStatisticsDtos = reportFeeMonthStatisticsInnerServiceSMOImpl.queryReportFloorUnitFeeDetailSummary(reportFeeMonthStatisticsDto);
        } else {
            reportFeeMonthStatisticsDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeMonthStatisticsDto.getRow()), count, reportFeeMonthStatisticsDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
