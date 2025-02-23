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

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.maintainance.MaintainancePlanMachineDto;
import com.newland.property.dto.maintainance.MaintainancePlanStaffDto;
import com.newland.property.intf.community.IMaintainancePlanMachineV1InnerServiceSMO;
import com.newland.property.intf.community.IMaintainancePlanStaffV1InnerServiceSMO;
import com.newland.property.intf.community.IMaintainancePlanV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.newland.property.dto.maintainance.MaintainancePlanDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：maintainancePlan.listMaintainancePlan
 * 请求路劲：/app/maintainancePlan.ListMaintainancePlan
 * add by 吴学文 at 2022-11-07 02:07:55 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "maintainancePlan.listMaintainancePlan")
public class ListMaintainancePlanCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListMaintainancePlanCmd.class);

    @Autowired
    private IMaintainancePlanV1InnerServiceSMO maintainancePlanV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanStaffV1InnerServiceSMO maintainancePlanStaffV1InnerServiceSMOImpl;

    @Autowired
    private IMaintainancePlanMachineV1InnerServiceSMO maintainancePlanMachineV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        MaintainancePlanDto maintainancePlanDto = BeanConvertUtil.covertBean(reqJson, MaintainancePlanDto.class);
        int count = maintainancePlanV1InnerServiceSMOImpl.queryMaintainancePlansCount(maintainancePlanDto);
        List<MaintainancePlanDto> maintainancePlanDtos = null;
        if (count > 0) {
            maintainancePlanDtos = maintainancePlanV1InnerServiceSMOImpl.queryMaintainancePlans(maintainancePlanDto);
            freshStaffAndMachineCount(maintainancePlanDtos);
        } else {
            maintainancePlanDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, maintainancePlanDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void freshStaffAndMachineCount(List<MaintainancePlanDto> maintainancePlanDtos) {
        if (maintainancePlanDtos == null || maintainancePlanDtos.size() < 1) {
            return;
        }
        List<String> planIds = new ArrayList<>();
        for (MaintainancePlanDto maintainancePlanDto : maintainancePlanDtos) {
            planIds.add(maintainancePlanDto.getPlanId());
        }
        MaintainancePlanMachineDto maintainancePlanMachineDto = new MaintainancePlanMachineDto();
        maintainancePlanMachineDto.setPlanIds(planIds.toArray(new String[planIds.size()]));
        maintainancePlanMachineDto.setCommunityId(maintainancePlanDtos.get(0).getCommunityId());
        List<MaintainancePlanMachineDto> maintainancePlanMachineDtos = maintainancePlanMachineV1InnerServiceSMOImpl.queryMaintainancePlanMachinesGroupCount(maintainancePlanMachineDto);
        if (maintainancePlanMachineDtos != null && maintainancePlanMachineDtos.size() > 0) {
            for (MaintainancePlanDto maintainancePlanDto : maintainancePlanDtos) {
                for (MaintainancePlanMachineDto tmpMaintainancePlanMachineDto : maintainancePlanMachineDtos) {
                    if (maintainancePlanDto.getPlanId().equals(tmpMaintainancePlanMachineDto.getPlanId())) {
                        maintainancePlanDto.setMachineCount(tmpMaintainancePlanMachineDto.getMachineCount());
                    }
                }
            }
        }
        MaintainancePlanStaffDto maintainancePlanStaffDto = new MaintainancePlanStaffDto();
        maintainancePlanStaffDto.setPlanIds(planIds.toArray(new String[planIds.size()]));
        maintainancePlanStaffDto.setCommunityId(maintainancePlanDtos.get(0).getCommunityId());
        List<MaintainancePlanStaffDto> maintainancePlanStaffDtos = maintainancePlanStaffV1InnerServiceSMOImpl.queryMaintainancePlanStaffsGroupCount(maintainancePlanStaffDto);
        if (maintainancePlanStaffDtos != null && maintainancePlanStaffDtos.size() > 0) {
            for (MaintainancePlanDto maintainancePlanDto : maintainancePlanDtos) {
                for (MaintainancePlanStaffDto tmpMaintainancePlanStaffDto : maintainancePlanStaffDtos) {
                    if (maintainancePlanDto.getPlanId().equals(tmpMaintainancePlanStaffDto.getPlanId())) {
                        maintainancePlanDto.setStaffCount(tmpMaintainancePlanStaffDto.getStaffCount());
                    }
                }
            }
        }
    }
}
