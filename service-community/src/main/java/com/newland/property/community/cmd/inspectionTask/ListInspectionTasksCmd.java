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
package com.newland.property.community.cmd.inspectionTask;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionTaskDto;
import com.newland.property.intf.community.IInspectionTaskInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.api.inspectionTask.ApiInspectionTaskDataVo;
import com.newland.property.vo.api.inspectionTask.ApiInspectionTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类表述：查询
 * 服务编码：inspectionTask.listInspectionTask
 * 请求路劲：/app/inspectionTask.ListInspectionTask
 * add by 吴学文 at 2022-03-16 09:35:12 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "inspectionTask.listInspectionTasks")
public class ListInspectionTasksCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListInspectionTasksCmd.class);

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        InspectionTaskDto inspectionTaskDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDto.class);

        if (reqJson.containsKey("moreState") && reqJson.getString("moreState").contains(",")) {
            inspectionTaskDto.setStates(reqJson.getString("moreState").split(","));
        }

        //查询当天巡检任务标识
        if (!StringUtil.isEmpty(reqJson.getString("isToday"))) {
            inspectionTaskDto.setDayTask(reqJson.getString("isToday"));
        }
        int count = inspectionTaskInnerServiceSMOImpl.queryInspectionTasksCount(inspectionTaskDto);

        List<ApiInspectionTaskDataVo> inspectionTasks = null;

        if (count > 0) {
            inspectionTasks = BeanConvertUtil.covertBeanList(inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto), ApiInspectionTaskDataVo.class);
        } else {
            inspectionTasks = new ArrayList<>();
        }

        ApiInspectionTaskVo apiInspectionTaskVo = new ApiInspectionTaskVo();

        apiInspectionTaskVo.setTotal(count);
        apiInspectionTaskVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionTaskVo.setInspectionTasks(inspectionTasks);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionTaskVo), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);

    }
}
