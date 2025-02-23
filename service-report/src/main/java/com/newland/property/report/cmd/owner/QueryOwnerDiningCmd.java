package com.newland.property.report.cmd.owner;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.intf.report.IReportOrderStatisticsInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业主 家庭成员 就餐明细
 */
@NewlandPropertyCmd(serviceCode = "owner.queryOwnerDining")
public class QueryOwnerDiningCmd extends Cmd {

    @Autowired
    private IReportOrderStatisticsInnerServiceSMO reportOrderStatisticsInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");

        if(reqJson.containsKey("startDate") && reqJson.containsKey("endDate")) {
            String startDate = reqJson.getString("startDate");
            String endDate = reqJson.getString("endDate");
            if (!StringUtil.isEmpty(startDate) && !startDate.contains(":")) {
                startDate += " 00:00:00";
                reqJson.put("startDate", startDate);
            }
            if (!StringUtil.isEmpty(endDate) && !endDate.contains(":")) {
                endDate += " 23:59:59";
                reqJson.put("endDate", endDate);
            }
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        int row = reqJson.getInteger("row");
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);

        // todo 查询总数量
        int total = reportOrderStatisticsInnerServiceSMOImpl.getOwnerDiningCount(ownerDto);
//        int count = 0;
        List<Map> infos = null;
        if (total > 0) {
            infos = reportOrderStatisticsInnerServiceSMOImpl.getOwnerDinings(ownerDto);
        } else {
            infos = new ArrayList<>();
        }

        ResponseEntity<String> responseEntity = ResultVo.createResponseEntity((int) Math.ceil((double) total / (double) row), total, infos);
        context.setResponseEntity(responseEntity);
    }
}
