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
package com.newland.property.community.cmd.visit;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.oaWorkflow.OaWorkflowDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.oaWorkflow.WorkflowModelDto;
import com.newland.property.intf.common.IWorkflowInnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.intf.community.IVisitSettingV1InnerServiceSMO;
import com.newland.property.intf.oa.IOaWorkflowInnerServiceSMO;
import com.newland.property.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.newland.property.po.oaWorkflow.OaWorkflowPo;
import com.newland.property.po.oaWorkflow.OaWorkflowXmlPo;
import com.newland.property.po.visit.VisitSettingPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.BpmnXml;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：visitSetting.saveVisitSetting
 * 请求路劲：/app/visitSetting.SaveVisitSetting
 * add by 吴学文 at 2023-01-18 14:43:29 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "visit.saveVisitSetting")
public class SaveVisitSettingCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveVisitSettingCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IVisitSettingV1InnerServiceSMO visitSettingV1InnerServiceSMOImpl;

    @Autowired
    private IWorkflowInnerServiceSMO workflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "typeName", "请求报文中未包含typeName");
        Assert.hasKeyAndValue(reqJson, "faceWay", "请求报文中未包含faceWay");
        Assert.hasKeyAndValue(reqJson, "carNumWay", "请求报文中未包含carNumWay");
//        Assert.hasKeyAndValue(reqJson, "paId", "请求报文中未包含paId");

        Assert.hasKeyAndValue(reqJson, "auditWay", "请求报文中未包含auditWay");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        String storeId = cmdDataFlowContext.getReqHeaders().get("store-id");
        OaWorkflowPo oaWorkflowPo = new OaWorkflowPo();
        oaWorkflowPo.setStoreId(storeId);
        oaWorkflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        oaWorkflowPo.setFlowName(reqJson.getString("typeName") + "审批流程");
        oaWorkflowPo.setFlowType(OaWorkflowDto.FLOW_TYPE_VISIT);
        //创建model
        WorkflowModelDto workflowModelDto = new WorkflowModelDto();
        workflowModelDto.setName(oaWorkflowPo.getFlowName());
        workflowModelDto.setKey(oaWorkflowPo.getFlowId());
        workflowModelDto = workflowInnerServiceSMOImpl.createModel(workflowModelDto);
        oaWorkflowPo.setModelId(workflowModelDto.getModelId());
        oaWorkflowPo.setFlowKey(workflowModelDto.getKey());
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        int flag = oaWorkflowInnerServiceSMOImpl.saveOaWorkflow(oaWorkflowPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //默认 流程图以防画错
        OaWorkflowXmlPo oaWorkflowXmlPo = new OaWorkflowXmlPo();
        oaWorkflowXmlPo.setStoreId(storeId);
        oaWorkflowXmlPo.setFlowId(oaWorkflowPo.getFlowId());
        oaWorkflowXmlPo.setXmlId(GenerateCodeFactory.getGeneratorId("79"));
        oaWorkflowXmlPo.setSvgXml("");
        oaWorkflowXmlPo.setBpmnXml(BpmnXml.getDefaultVisitBpmnXml(oaWorkflowPo.getFlowId()));

        flag = oaWorkflowXmlInnerServiceSMOImpl.saveOaWorkflowXml(oaWorkflowXmlPo);
        if (flag < 1) {
            throw new CmdException("保存模型数据失败");
        }

        VisitSettingPo visitSettingPo = BeanConvertUtil.covertBean(reqJson, VisitSettingPo.class);
        visitSettingPo.setSettingId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        visitSettingPo.setFlowId(oaWorkflowPo.getFlowId());
        visitSettingPo.setFlowName(oaWorkflowPo.getFlowName());
        if (StringUtil.isEmpty(reqJson.getString("visitNumber"))) {
            visitSettingPo.setVisitNumber("0");
        }
        if (!StringUtil.isEmpty(visitSettingPo.getPaId())) {
            ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
            parkingAreaDto.setCommunityId(reqJson.getString("communityId"));
            parkingAreaDto.setPaId(reqJson.getString("paId"));
            List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
            Assert.listOnlyOne(parkingAreaDtos, "停车场不存在");
            visitSettingPo.setPaNum(parkingAreaDtos.get(0).getNum());
        }
        flag = visitSettingV1InnerServiceSMOImpl.saveVisitSetting(visitSettingPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
