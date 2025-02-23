package com.newland.property.dev.cmd.task;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.task.TaskTemplateSpecDto;
import com.newland.property.intf.job.ITaskInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "task.listTaskTemplateSpec")
public class ListTaskTemplateSpecCmd extends Cmd {

    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        TaskTemplateSpecDto taskTemplateSpecDto = BeanConvertUtil.covertBean(reqJson, TaskTemplateSpecDto.class);

        int count = taskInnerServiceSMOImpl.queryTaskTemplateSpecCount(taskTemplateSpecDto);

        List<TaskTemplateSpecDto> taskTemplateSpecDtos = null;

        if (count > 0) {
            taskTemplateSpecDtos = taskInnerServiceSMOImpl.queryTaskTemplateSpec(taskTemplateSpecDto);
        } else {
            taskTemplateSpecDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, taskTemplateSpecDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }
}
