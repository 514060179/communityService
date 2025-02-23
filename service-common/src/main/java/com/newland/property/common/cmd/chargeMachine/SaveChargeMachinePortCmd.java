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
package com.newland.property.common.cmd.chargeMachine;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.charge.ChargeMachinePortDto;
import com.newland.property.intf.common.IChargeMachinePortV1InnerServiceSMO;
import com.newland.property.po.charge.ChargeMachinePortPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：chargeMachinePort.saveChargeMachinePort
 * 请求路劲：/app/chargeMachinePort.SaveChargeMachinePort
 * add by 吴学文 at 2023-03-02 01:17:43 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "chargeMachine.saveChargeMachinePort")
public class SaveChargeMachinePortCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveChargeMachinePortCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IChargeMachinePortV1InnerServiceSMO chargeMachinePortV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "machineId", "请求报文中未包含machineId");
        Assert.hasKeyAndValue(reqJson, "portName", "请求报文中未包含portName");
        Assert.hasKeyAndValue(reqJson, "portCode", "请求报文中未包含portCode");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        ChargeMachinePortDto chargeMachinePortDto = new ChargeMachinePortDto();
        chargeMachinePortDto.setMachineId(reqJson.getString("machineId"));
        chargeMachinePortDto.setPortCode(reqJson.getString("portCode"));
        chargeMachinePortDto.setCommunityId(reqJson.getString("communityId"));
        int flag = chargeMachinePortV1InnerServiceSMOImpl.queryChargeMachinePortsCount(chargeMachinePortDto);
        if (flag > 0) {
            throw new CmdException("插座已经存在");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        ChargeMachinePortPo chargeMachinePortPo = BeanConvertUtil.covertBean(reqJson, ChargeMachinePortPo.class);
        chargeMachinePortPo.setPortId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = chargeMachinePortV1InnerServiceSMOImpl.saveChargeMachinePort(chargeMachinePortPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
