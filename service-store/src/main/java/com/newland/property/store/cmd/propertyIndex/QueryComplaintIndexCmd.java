package com.newland.property.store.cmd.propertyIndex;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.intf.store.IComplaintInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

@NewlandPropertyCmd(serviceCode = "propertyIndex.queryComplaintIndex")
public class QueryComplaintIndexCmd extends Cmd {

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject paramOut = new JSONObject();

        // 全部投诉
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        int allCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        paramOut.put("allComplaintCount", allCount);

        // 待处理
        complaintDto = new ComplaintDto();
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setState(ComplaintDto.STATE_WAIT);
        int waitCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        paramOut.put("waitComplaintCount", waitCount);


        //已完成
        complaintDto = new ComplaintDto();
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setState(ComplaintDto.STATE_FINISH);
        int finishCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        paramOut.put("finishComplaintCount", finishCount);
        context.setResponseEntity(ResultVo.createResponseEntity(paramOut));
    }
}
