package com.newland.property.report.cmd.reportInfoAnswer;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.reportInfo.ReportInfoAnswerDto;
import com.newland.property.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "/reportInfoAnswer/queryReportInfoAnswerByOpenId")
public class QueryReportInfoAnswerByOpenId extends Cmd {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "openId", "请求报文中未包含openId");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ReportInfoAnswerDto reportInfoAnswerDto = new ReportInfoAnswerDto();
        reportInfoAnswerDto.setOpenId(reqJson.getString("openId"));
        reportInfoAnswerDto.setPage(1);
        reportInfoAnswerDto.setRow(1);
        List<ReportInfoAnswerDto> reportInfoAnswerDtos = reportInfoAnswerInnerServiceSMOImpl.queryReportInfoAnswers(reportInfoAnswerDto);

        cmdDataFlowContext.setResponseEntity(ResultVo.createResponseEntity(reportInfoAnswerDtos));
    }
}
