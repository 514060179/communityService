package com.newland.property.job.cmd.job;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.task.TaskAttrDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.dev.ITaskAttrV1InnerServiceSMO;
import com.newland.property.intf.dev.ITaskV1InnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 手工运行job
 */
@NewlandPropertyCmd(serviceCode = "job.runJob")
public class RunJobCmd extends Cmd {

    @Autowired
    private ITaskV1InnerServiceSMO taskV1InnerServiceSMOImpl;

    @Autowired
    private ITaskAttrV1InnerServiceSMO taskAttrV1InnerServiceSMO;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "taskId", "未包含任务ID");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(reqJson.getString("taskId"));
        List<TaskDto> taskDtos = taskV1InnerServiceSMOImpl.queryTasks(taskDto);

        Assert.listOnlyOne(taskDtos,"任务不存在");
        TaskAttrDto taskAttrDto = new TaskAttrDto();
        taskAttrDto.setTaskId(taskDtos.get(0).getTaskId());
        List<TaskAttrDto> taskAttrDtos = taskAttrV1InnerServiceSMO.queryTaskAttrs(taskAttrDto);
        taskDto.setTaskAttr(taskAttrDtos);


        TaskSystemQuartz taskSystemQuartz = (TaskSystemQuartz) ApplicationContextFactory.getBean(taskDtos.get(0).getClassBean());
        try {
            taskSystemQuartz.startTask(taskDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CmdException(e.getMessage());
        }

    }
}
