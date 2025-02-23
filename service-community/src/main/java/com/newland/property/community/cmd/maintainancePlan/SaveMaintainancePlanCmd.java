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
package com.newland.property.community.cmd.maintainancePlan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.machine.EquipmentAccountDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IEquipmentAccountV1InnerServiceSMO;
import com.newland.property.intf.community.IMaintainancePlanMachineV1InnerServiceSMO;
import com.newland.property.intf.community.IMaintainancePlanStaffV1InnerServiceSMO;
import com.newland.property.intf.community.IMaintainancePlanV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.maintainance.MaintainancePlanPo;
import com.newland.property.po.maintainance.MaintainancePlanMachinePo;
import com.newland.property.po.maintainance.MaintainancePlanStaffPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：maintainancePlan.saveMaintainancePlan
 * 请求路劲：/app/maintainancePlan.SaveMaintainancePlan
 * add by 吴学文 at 2022-11-07 02:07:55 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "maintainancePlan.saveMaintainancePlan")
public class SaveMaintainancePlanCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveMaintainancePlanCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMaintainancePlanV1InnerServiceSMO maintainancePlanV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanMachineV1InnerServiceSMO maintainancePlanMachineV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanStaffV1InnerServiceSMO maintainancePlanStaffV1InnerServiceSMOImpl;

    @Autowired
    private IEquipmentAccountV1InnerServiceSMO equipmentAccountV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "planName", "请求报文中未包含planName");
        Assert.hasKeyAndValue(reqJson, "standardId", "请求报文中未包含standardId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "startDate", "请求报文中未包含startDate");
        Assert.hasKeyAndValue(reqJson, "endDate", "请求报文中未包含endDate");
        Assert.hasKeyAndValue(reqJson, "planPeriod", "请求报文中未包含planPeriod");

        if (!reqJson.containsKey("staffs")) {
            throw new CmdException("未包含员工");
        }

        JSONArray staffs = reqJson.getJSONArray("staffs");

        if (staffs.size() < 1) {
            throw new CmdException("未包含员工");
        }

        if (!reqJson.containsKey("machineIds")) {
            throw new CmdException("未包含设备");
        }

        JSONArray machineIds = reqJson.getJSONArray("machineIds");

        if (machineIds.size() < 1) {
            throw new CmdException("未包含设备");
        }


    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = cmdDataFlowContext.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setRow(1);
        userDto.setPage(1);
        List<UserDto> userDtos =  userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos,"员工不存在");

        MaintainancePlanPo maintainancePlanPo = BeanConvertUtil.covertBean(reqJson, MaintainancePlanPo.class);
        maintainancePlanPo.setCreateUserId(userId);
        maintainancePlanPo.setCreateUserName(userDtos.get(0).getName());
        maintainancePlanPo.setPlanId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = maintainancePlanV1InnerServiceSMOImpl.saveMaintainancePlan(maintainancePlanPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        JSONArray staffs = reqJson.getJSONArray("staffs");
        MaintainancePlanStaffPo maintainancePlanStaffPo = null;
        for (int staffIndex = 0; staffIndex < staffs.size(); staffIndex++) {
            maintainancePlanStaffPo = new MaintainancePlanStaffPo();
            maintainancePlanStaffPo.setCommunityId(reqJson.getString("communityId"));
            maintainancePlanStaffPo.setPlanId(maintainancePlanPo.getPlanId());
            maintainancePlanStaffPo.setMpsId(GenerateCodeFactory.getGeneratorId("11"));
            maintainancePlanStaffPo.setStaffId(staffs.getJSONObject(staffIndex).getString("userId"));
            maintainancePlanStaffPo.setStaffName(staffs.getJSONObject(staffIndex).getString("name"));
            maintainancePlanStaffV1InnerServiceSMOImpl.saveMaintainancePlanStaff(maintainancePlanStaffPo);
        }

        JSONArray machineIds = reqJson.getJSONArray("machineIds");
        MaintainancePlanMachinePo maintainancePlanMachinePo = null;
        EquipmentAccountDto equipmentAccountDto = null;
        List<EquipmentAccountDto> accountDtos = null;
        for (int machineIndex = 0; machineIndex < machineIds.size(); machineIndex++) {
            maintainancePlanMachinePo = new MaintainancePlanMachinePo();
            maintainancePlanMachinePo.setCommunityId(reqJson.getString("communityId"));
            maintainancePlanMachinePo.setPlanId(maintainancePlanPo.getPlanId());
            maintainancePlanMachinePo.setMpmId(GenerateCodeFactory.getGeneratorId("11"));
            maintainancePlanMachinePo.setMachineId(machineIds.getString(machineIndex));

            equipmentAccountDto = new EquipmentAccountDto();
            equipmentAccountDto.setMachineId(machineIds.getString(machineIndex));
            accountDtos = equipmentAccountV1InnerServiceSMOImpl.queryEquipmentAccounts(equipmentAccountDto);
            Assert.listOnlyOne(accountDtos, "设备不存在");

            maintainancePlanMachinePo.setMachineName(accountDtos.get(0).getMachineName());
            maintainancePlanMachineV1InnerServiceSMOImpl.saveMaintainancePlanMachine(maintainancePlanMachinePo);
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
