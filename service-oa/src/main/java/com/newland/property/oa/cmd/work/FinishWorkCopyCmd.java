package com.newland.property.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.workCopy.WorkCopyDto;
import com.newland.property.dto.workPool.WorkPoolDto;
import com.newland.property.intf.oa.IWorkCopyV1InnerServiceSMO;
import com.newland.property.po.workCopy.WorkCopyPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "work.finishWorkCopy")
public class FinishWorkCopyCmd extends Cmd {

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "copyId", "未包含抄送人");
        Assert.hasKeyAndValue(reqJson, "auditMessage", "未包含说明");
        String userId = CmdContextUtils.getUserId(context);
        WorkCopyDto workCopyDto = new WorkCopyDto();
        workCopyDto.setCopyId(reqJson.getString("copyId"));
        workCopyDto.setState(WorkPoolDto.STATE_DOING);
        workCopyDto.setStaffId(userId);
        List<WorkCopyDto> workCopyDtos = workCopyV1InnerServiceSMOImpl.queryWorkCopys(workCopyDto);

        if (ListUtil.isNull(workCopyDtos)) {
            throw new CmdException("抄送单不存在");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        WorkCopyPo workCopyPo = new WorkCopyPo();
        workCopyPo.setCopyId(reqJson.getString("copyId"));
        workCopyPo.setState(WorkPoolDto.STATE_COMPLETE);
        workCopyPo.setRemark(reqJson.getString("auditMessage"));
        workCopyV1InnerServiceSMOImpl.updateWorkCopy(workCopyPo);
    }
}
