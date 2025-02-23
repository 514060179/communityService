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
package com.newland.property.common.cmd.workflow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.oaWorkflow.WorkflowDto;
import com.newland.property.dto.oaWorkflow.WorkflowStepDto;
import com.newland.property.dto.oaWorkflow.WorkflowStepStaffDto;
import com.newland.property.intf.common.*;
import com.newland.property.po.oaWorkflow.WorkflowPo;
import com.newland.property.po.oaWorkflow.WorkflowStepPo;
import com.newland.property.po.oaWorkflow.WorkflowStepStaffPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * 类表述：更新
 * 服务编码：workflow.updateWorkflow
 * 请求路劲：/app/workflow.UpdateWorkflow
 * add by 吴学文 at 2021-09-18 13:35:13 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "workflow.updateWorkflow")
public class UpdateWorkflowCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateWorkflowCmd.class);


    @Autowired
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;


    @Autowired
    private IWorkflowStepStaffInnerServiceSMO workflowStepStaffInnerServiceSMOImpl;


    @Autowired
    private IWorkflowStepStaffV1InnerServiceSMO workflowStepStaffV1InnerServiceSMOImpl;

    @Autowired
    private IWorkflowStepV1InnerServiceSMO workflowStepV1InnerServiceSMOImpl;

    @Autowired
    private IWorkflowStepInnerServiceSMO workflowStepInnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "flowId", "flowId不能为空");
        Assert.hasKeyAndValue(reqJson, "flowName", "请求报文中未包含flowName");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "storeId", "请求报文中未包含商户ID");

        JSONArray steps = reqJson.getJSONArray("steps");
        if (steps == null || steps.size() < 1) {
            //throw new IllegalArgumentException("未包含步骤");
            return;
        }
        JSONObject step = null;
        JSONObject subStaff = null;
        for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
            step = steps.getJSONObject(stepIndex);

            Assert.hasKeyAndValue(step, "staffId", "步骤中未包含员工");
            Assert.hasKeyAndValue(step, "staffName", "步骤中未包含员工");
            Assert.hasKeyAndValue(step, "type", "步骤中类型会签还是正常流程");

            //正常流程
            if (WorkflowStepDto.TYPE_NORMAL.equals(step.getString("type"))) {
                continue;
            }

            //会签流程
            if (!step.containsKey("subStaff")) {
                throw new IllegalArgumentException("未包含会签员工信息");
            }

            JSONArray subStaffs = step.getJSONArray("subStaff");

            if (subStaffs == null || subStaffs.size() < 1) {
                throw new IllegalArgumentException("未包含会签员工信息");
            }

            for (int subStaffIndex = 0; subStaffIndex < subStaffs.size(); subStaffIndex++) {
                subStaff = subStaffs.getJSONObject(subStaffIndex);
                Assert.hasKeyAndValue(subStaff, "staffId", "会签中未包含员工");
                Assert.hasKeyAndValue(subStaff, "staffName", "会签中未包含员工");
            }
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        logger.debug("进入修改工作流cmd>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        if (reqJson.containsKey("flowType")) {
            String flowType = reqJson.getString("flowType");
            if (WorkflowDto.FLOW_TYPE_PURCHASE.equals(flowType)
                    || WorkflowDto.FLOW_TYPE_CONTRACT_CHANGE.equals(flowType)
                    || WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE.equals(flowType)
                    || WorkflowDto.FLOW_TYPE_CONTRACT_APPLY.equals(flowType)) {
                reqJson.put("communityId", "9999");
            }
        }

        WorkflowStepDto workflowStepDto = new WorkflowStepDto();
        workflowStepDto.setFlowId(reqJson.getString("flowId"));
        workflowStepDto.setCommunityId(reqJson.getString("communityId"));
        List<WorkflowStepDto> workflowStepDtos = workflowStepInnerServiceSMOImpl.queryWorkflowSteps(workflowStepDto);
        if (workflowStepDtos != null) {
            for (WorkflowStepDto tmpWorkflowStepDto : workflowStepDtos) {
                deleteWorkflowStepAndStaff(cmdDataFlowContext, reqJson, tmpWorkflowStepDto);
            }
        }

        //修改 工作流程
        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setFlowId(reqJson.getString("flowId"));
        workflowPo.setFlowName(reqJson.getString("flowName"));
        workflowPo.setCommunityId(reqJson.getString("communityId"));
        workflowPo.setStartNodeFinish(reqJson.getString("startNodeFinish"));
        workflowPo.setDescrible(reqJson.getString("describle"));


        //保存 工作流程步骤
        JSONArray steps = reqJson.getJSONArray("steps");
        String processDefinitionKey = "";
        int flag = 0;
        if (steps != null && steps.size() > 0) { // 有步骤
            JSONObject step = null;
            JSONObject subStaff = null;
            WorkflowStepStaffPo workflowStepStaffPo = null;
            List<WorkflowStepDto> tmpWorkflowStepDtos = new ArrayList<>();
            for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
                step = steps.getJSONObject(stepIndex);
                WorkflowStepPo workflowStepPo = new WorkflowStepPo();
                workflowStepPo.setStepId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
                workflowStepPo.setCommunityId(workflowPo.getCommunityId());
                workflowStepPo.setFlowId(workflowPo.getFlowId());
                workflowStepPo.setSeq((stepIndex + 1) + "");
                workflowStepPo.setType(step.getString("type"));
                workflowStepPo.setStoreId(reqJson.getString("storeId"));
                flag = workflowStepV1InnerServiceSMOImpl.saveWorkflowStep(workflowStepPo);
                if (flag < 1) {
                    throw new CmdException("保存步骤失败");
                }
                WorkflowStepDto tmpWorkflowStepDto = BeanConvertUtil.covertBean(workflowStepPo, WorkflowStepDto.class);
                //正常流程
                List<WorkflowStepStaffDto> workflowStepStaffDtos = new ArrayList<>();
                workflowStepStaffPo = new WorkflowStepStaffPo();
                workflowStepStaffPo.setWssId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_wssId));
                workflowStepStaffPo.setCommunityId(workflowPo.getCommunityId());
                workflowStepStaffPo.setStaffId(step.getString("staffId"));
                workflowStepStaffPo.setStaffName(step.getString("staffName"));
                workflowStepStaffPo.setStepId(workflowStepPo.getStepId());
                workflowStepStaffPo.setFlowType(reqJson.getString("flowType"));
                workflowStepStaffPo.setStaffRole(StringUtil.isEmpty(step.getString("staffRole")) ? "1001" : step.getString("staffRole"));

                if (!"1001".equals(workflowStepStaffPo.getStaffRole()) && workflowStepStaffPo.getStaffRole().startsWith("${")) {
                    throw new IllegalArgumentException("采购人员或者物品领用人员必须指定人，不能是动态指定");
                }
                flag = workflowStepStaffV1InnerServiceSMOImpl.saveWorkflowStepStaff(workflowStepStaffPo);
                if (flag < 1) {
                    throw new CmdException("保存步骤失败");
                }
                workflowStepStaffDtos.add(BeanConvertUtil.covertBean(workflowStepStaffPo, WorkflowStepStaffDto.class));
                //会签流程
                JSONArray subStaffs = step.getJSONArray("subStaff");
                if (subStaffs != null && subStaffs.size() > 0) {
                    for (int subStaffIndex = 0; subStaffIndex < subStaffs.size(); subStaffIndex++) {
                        subStaff = subStaffs.getJSONObject(subStaffIndex);
                        workflowStepStaffPo = new WorkflowStepStaffPo();
                        workflowStepStaffPo.setWssId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_wssId));
                        workflowStepStaffPo.setCommunityId(workflowPo.getCommunityId());
                        workflowStepStaffPo.setStaffId(subStaff.getString("staffId"));
                        workflowStepStaffPo.setStaffName(subStaff.getString("staffName"));
                        workflowStepStaffPo.setStepId(workflowStepPo.getStepId());
                        workflowStepStaffPo.setStaffRole(StringUtil.isEmpty(subStaff.getString("staffRole")) ? "1001" : subStaff.getString("staffRole"));

                        flag = workflowStepStaffV1InnerServiceSMOImpl.saveWorkflowStepStaff(workflowStepStaffPo);
                        if (flag < 1) {
                            throw new CmdException("保存步骤失败");
                        }
                        workflowStepStaffDtos.add(BeanConvertUtil.covertBean(workflowStepStaffPo, WorkflowStepStaffDto.class));
                    }
                }

                tmpWorkflowStepDto.setWorkflowStepStaffs(workflowStepStaffDtos);

                tmpWorkflowStepDtos.add(tmpWorkflowStepDto);
            }

            WorkflowDto workflowDto = BeanConvertUtil.covertBean(workflowPo, WorkflowDto.class);
            workflowDto.setWorkflowSteps(tmpWorkflowStepDtos);
            WorkflowDto tmpWorkflowDto = workflowInnerServiceSMOImpl.addFlowDeployment(workflowDto);
            processDefinitionKey = tmpWorkflowDto.getProcessDefinitionKey();
        } else {
            processDefinitionKey = "-1";
        }

        workflowPo.setProcessDefinitionKey(processDefinitionKey);
        flag = workflowV1InnerServiceSMOImpl.updateWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("保存步骤失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void deleteWorkflowStepAndStaff(ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson, WorkflowStepDto workflowStepDto) {
        WorkflowStepStaffDto workflowStepStaffDto = new WorkflowStepStaffDto();
        workflowStepStaffDto.setStepId(workflowStepDto.getStepId());
        workflowStepStaffDto.setCommunityId(workflowStepDto.getCommunityId());
        List<WorkflowStepStaffDto> workflowStepStaffDtos = workflowStepStaffInnerServiceSMOImpl.queryWorkflowStepStaffs(workflowStepStaffDto);

        WorkflowStepPo workflowStepPo = new WorkflowStepPo();
        workflowStepPo.setCommunityId(workflowStepDto.getCommunityId());
        workflowStepPo.setStepId(workflowStepDto.getStepId());
        int flag = workflowStepV1InnerServiceSMOImpl.deleteWorkflowStep(workflowStepPo);
        if (flag < 1) {
            throw new CmdException("保存步骤失败");
        }
        if (workflowStepStaffDtos == null) {
            return;
        }
        for (WorkflowStepStaffDto tmpWorkflowStepStaffDto : workflowStepStaffDtos) {
            WorkflowStepStaffPo workflowStepStaffPo = new WorkflowStepStaffPo();
            workflowStepStaffPo.setCommunityId(workflowStepDto.getCommunityId());
            workflowStepStaffPo.setWssId(tmpWorkflowStepStaffDto.getWssId());
            flag = workflowStepStaffV1InnerServiceSMOImpl.deleteWorkflowStepStaff(workflowStepStaffPo);
            if (flag < 1) {
                throw new CmdException("保存步骤失败");
            }
        }
    }
}
