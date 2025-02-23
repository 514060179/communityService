package com.newland.property.dev.cmd.task;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.job.ITaskInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "task.startTask")
public class StartTaskCmd  extends Cmd {

    @Autowired
    private ITaskInnerServiceSMO taskInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "taskId", "请求报文中未包含taskName");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(reqJson.getString("taskId"));
        List<TaskDto> taskDtos = taskInnerServiceSMOImpl.queryTasks(taskDto);
        ResultVo resultVo = null;
        if (taskDtos == null || taskDtos.size() < 1) {
            resultVo = new ResultVo(ResultVo.ORDER_ERROR, "传入任务ID错误");
            ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

            context.setResponseEntity(responseEntity);
            return;
        }
        int state = taskInnerServiceSMOImpl.startTask(taskDtos.get(0));

        if(state >0){
            resultVo = new ResultVo(ResultVo.CODE_OK, "启动成功");
        }else{
            resultVo = new ResultVo(ResultVo.ORDER_ERROR, "启动失败");
        }
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
