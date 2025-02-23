package com.newland.property.common.cmd.auditUser;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.intf.common.IComplaintUserInnerServiceSMO;
import com.newland.property.intf.store.IComplaintV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "auditUser.listAuditHistoryComplaints")
public class ListAuditHistoryComplaintsCmd extends Cmd {

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区ID");
        Assert.hasKeyAndValue(reqJson, "row", "必填，请填写每页显示数");
        Assert.hasKeyAndValue(reqJson, "page", "必填，请填写页数");

        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = CmdContextUtils.getUserId(context);

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStaffId(userId);
        complaintDto.setStoreId(reqJson.getString("storeId"));
        complaintDto.setCommunityId(reqJson.getString("communityId"));
        complaintDto.setPage(reqJson.getInteger("page"));
        complaintDto.setRow(reqJson.getInteger("row"));
        complaintDto.setState(ComplaintDto.STATE_FINISH);

        long count = complaintV1InnerServiceSMOImpl.queryStaffComplaintCount(complaintDto);

        List<ComplaintDto> complaintDtos = null;

        if (count > 0) {
            complaintDtos = complaintV1InnerServiceSMOImpl.queryStaffComplaints(complaintDto);
        } else {
            complaintDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
