/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.community.cmd.maintainanceTask;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.maintainance.MaintainanceTaskDto;
import com.newland.property.intf.community.IMaintainanceTaskV1InnerServiceSMO;
import com.newland.property.po.maintainance.MaintainanceTaskPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：maintainanceTask.updateMaintainanceTask
 * 请求路劲：/app/maintainanceTask.UpdateMaintainanceTask
 * add by 吴学文 at 2022-11-08 15:51:01 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "maintainanceTask.updateMaintainanceTask")
public class UpdateMaintainanceTaskCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateMaintainanceTaskCmd.class);


    @Autowired
    private IMaintainanceTaskV1InnerServiceSMO maintainanceTaskV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "taskId", "taskId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        MaintainanceTaskDto maintainanceTaskDto1 = new MaintainanceTaskDto();
        maintainanceTaskDto1.setCommunityId(reqJson.getString("communityId"));
        maintainanceTaskDto1.setTaskId(reqJson.getString("taskId"));
        List<MaintainanceTaskDto> maintainanceTaskDtoList = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto1);
        if (maintainanceTaskDtoList.size()!= 1) {
            ResponseEntity<String> responseEntity = ResultVo.createResponseEntity(ResultVo.CODE_BUSINESS_VERIFICATION, "未找到保养任务信息或找到多条！");
            cmdDataFlowContext.setResponseEntity(responseEntity);
            return;
        }

        MaintainanceTaskDto maintainanceTaskDto = new MaintainanceTaskDto();
        maintainanceTaskDto.setTaskId(reqJson.getString("taskId"));
        maintainanceTaskDto.setCommunityId(reqJson.getString("communityId"));
        List<MaintainanceTaskDto> maintainanceTaskDtos = maintainanceTaskV1InnerServiceSMOImpl.queryMaintainanceTasks(maintainanceTaskDto);

        Assert.listOnlyOne(maintainanceTaskDtos, "未找到需要修改的保养任务 或多条数据");

        JSONObject businessMaintainanceTask = new JSONObject();
        businessMaintainanceTask.putAll(BeanConvertUtil.beanCovertMap(maintainanceTaskDtos.get(0)));

        MaintainanceTaskPo maintainanceTaskPo = BeanConvertUtil.covertBean(businessMaintainanceTask, MaintainanceTaskPo.class);
        maintainanceTaskPo.setActInsTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        maintainanceTaskPo.setActUserId(reqJson.getString("userId"));
        maintainanceTaskPo.setActUserName(reqJson.getString("userName"));
        maintainanceTaskPo.setState(reqJson.getString("state"));
        if(!StringUtil.isEmpty(reqJson.getString("taskType")) && "2000".equals(reqJson.getString("taskType")) ){
//            maintainanceTaskPo.setOriginalPlanUserId(maintainanceTaskPo.getPlanUserId());
//            maintainanceTaskPo.setOriginalPlanUserName(maintainanceTaskPo.getPlanUserName());
            maintainanceTaskPo.setPlanUserId(reqJson.getString("staffId"));
            maintainanceTaskPo.setPlanUserName(reqJson.getString("staffName"));
            maintainanceTaskPo.setTaskType(reqJson.getString("taskType"));
            maintainanceTaskPo.setTransferDesc(reqJson.getString("transferDesc"));
        }

        int flag = maintainanceTaskV1InnerServiceSMOImpl.updateMaintainanceTask(maintainanceTaskPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
