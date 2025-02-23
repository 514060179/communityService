package com.newland.property.common.cmd.workflow;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.oaWorkflow.WorkflowAuditInfoDto;
import com.newland.property.intf.common.IWorkflowInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
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

@NewlandPropertyCmd(serviceCode = "workflow.listWorkflowAuditInfo")
public class ListWorkflowAuditInfoCmd extends Cmd {


    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中请包含小区ID");
        Assert.hasKeyAndValue(reqJson, "businessKey", "请求报文中未包含业务ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        WorkflowAuditInfoDto workflowAuditInfoDto = BeanConvertUtil.covertBean(reqJson, WorkflowAuditInfoDto.class);
        List<WorkflowAuditInfoDto> workflowAuditInfoDtos = workflowInnerServiceSMOImpl.queryWorkflowAuditHistory(workflowAuditInfoDto);
        freshUserName(workflowAuditInfoDtos);
        ResultVo resultVo = new ResultVo(ResultVo.CODE_OK, ResultVo.MSG_OK, workflowAuditInfoDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void freshUserName(List<WorkflowAuditInfoDto> workflowAuditInfoDtos) {
        if (workflowAuditInfoDtos == null || workflowAuditInfoDtos.size() < 1) {
            return;
        }

        List<String> userIds = new ArrayList<>();
        for (WorkflowAuditInfoDto tmpWorkflowAuditInfoDto:workflowAuditInfoDtos ){
            userIds.add(tmpWorkflowAuditInfoDto.getUserId());
        }

        UserDto userDto = new UserDto();
        userDto.setUserIds(userIds.toArray(new String[userIds.size()]));
        List<UserDto> userDtos = userInnerServiceSMOImpl.getStaffs(userDto);


        for (WorkflowAuditInfoDto tmpWorkflowAuditInfoDto:workflowAuditInfoDtos ){
            if(StringUtil.isEmpty(tmpWorkflowAuditInfoDto.getUserId())){
                continue;
            }
            for(UserDto tmpUserDto: userDtos){
                if (tmpWorkflowAuditInfoDto.getUserId().equals(tmpUserDto.getUserId())){
                    tmpWorkflowAuditInfoDto.setUserName(tmpUserDto.getUserName());
                    tmpWorkflowAuditInfoDto.setOrgName(tmpUserDto.getOrgName());
                }
            }
        }

    }
}
