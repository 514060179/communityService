package com.newland.property.api.bmo.task.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.task.ITaskAttrBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.job.ITaskAttrInnerServiceSMO;
import com.newland.property.dto.task.TaskAttrDto;
import com.newland.property.po.task.TaskAttrPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskAttrBMOImpl")
public class TaskAttrBMOImpl extends ApiBaseBMO implements ITaskAttrBMO {

    @Autowired
    private ITaskAttrInnerServiceSMO taskAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addTaskAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("attrId", "-1");
        TaskAttrPo taskAttrPo = BeanConvertUtil.covertBean(paramInJson, TaskAttrPo.class);
        super.insert(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_TASK_ATTR);
    }


    /**
     * 添加活动信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateTaskAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TaskAttrDto taskAttrDto = new TaskAttrDto();
        taskAttrDto.setAttrId(paramInJson.getString("attrId"));
        taskAttrDto.setTaskId(paramInJson.getString("taskId"));
        List<TaskAttrDto> taskAttrDtos = taskAttrInnerServiceSMOImpl.queryTaskAttrs(taskAttrDto);

        Assert.listOnlyOne(taskAttrDtos, "未找到需要修改的活动 或多条数据");


        paramInJson.putAll(BeanConvertUtil.beanCovertMap(taskAttrDtos.get(0)));
        TaskAttrPo taskAttrPo = BeanConvertUtil.covertBean(paramInJson, TaskAttrPo.class);
        super.update(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_TASK_ATTR);
    }



    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteTaskAttr(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        TaskAttrPo taskAttrPo = BeanConvertUtil.covertBean(paramInJson, TaskAttrPo.class);
        super.update(dataFlowContext, taskAttrPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_TASK_ATTR);
    }

}
