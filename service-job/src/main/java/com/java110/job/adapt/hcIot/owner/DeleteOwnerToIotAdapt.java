/*
 * Copyright 2017-2020 吴学文 and java110 team.
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
package com.java110.job.adapt.hcIot.owner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.room.RoomDto;
import com.java110.dto.machine.MachineDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.system.Business;
import com.java110.intf.common.IMachineInnerServiceSMO;
import com.java110.intf.community.IRoomInnerServiceSMO;
import com.java110.intf.user.IOwnerInnerServiceSMO;
import com.java110.job.adapt.DatabusAdaptImpl;
import com.java110.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.java110.po.owner.OwnerPo;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * HC  添加业主同步iot
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "deleteOwnerToIotAdapt")
public class DeleteOwnerToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcMachineAsynImpl;
    @Autowired
    IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    /**
     * {
     * "userId": "702020042194860037",
     * "machineCode": "101010"
     * }
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        System.out.println("进入删除删除业主的databus 处理类，DeleteOwnerToIotAdapt,{}"+data.toJSONString());

        JSONArray  businessMachines = new JSONArray();
        if (data.containsKey(OwnerPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessMachines.add(bObj);
            } else if (bObj instanceof List) {
                businessMachines = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessMachines = (JSONArray) bObj;
            }

        }else {
            if (data instanceof JSONObject) {
                businessMachines.add(data);
            }
        }
        for (int bOwnerIndex = 0; bOwnerIndex < businessMachines.size(); bOwnerIndex++) {
            JSONObject businessOwner = businessMachines.getJSONObject(bOwnerIndex);
            doSendMachine(business, businessOwner);
        }
    }

    private void doSendMachine(Business business, JSONObject businessOwner) {

        OwnerPo ownerPo = BeanConvertUtil.covertBean(businessOwner, OwnerPo.class);

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        ownerDto.setMemberId(ownerPo.getMemberId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "未找到删除的业主信息");

        //拿到小区ID
        String communityId = ownerPo.getCommunityId();
        //根据小区ID查询现有设备
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(communityId);
        //String[] locationObjIds = new String[]{communityId};
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(communityId);

        RoomDto roomDto = new RoomDto();
        roomDto.setOwnerId(ownerPo.getOwnerId());
        roomDto.setStatusCd("");
        //这种情况说明 业主已经删掉了 需要查询状态为 1 的数据
        List<RoomDto> rooms = roomInnerServiceSMOImpl.queryRoomsByOwner(roomDto);

        if(rooms != null && rooms.size()>0) {
            for (RoomDto tRoomDto : rooms) {
                locationObjIds.add(tRoomDto.getUnitId());
                locationObjIds.add(tRoomDto.getRoomId());
                locationObjIds.add(tRoomDto.getFloorId());
            }
        }
        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        for (MachineDto tmpMachineDto : machineDtos) {
            if (!"9999".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            JSONObject postParameters = new JSONObject();
            postParameters.put("machineCode", tmpMachineDto.getMachineCode());
            postParameters.put("userId", ownerPo.getMemberId());
            postParameters.put("name", ownerDtos.get(0).getName());
            postParameters.put("extMachineId", tmpMachineDto.getMachineId());
            postParameters.put("extCommunityId", tmpMachineDto.getCommunityId());
            hcMachineAsynImpl.sendDeleteOwner(postParameters);
        }

    }
}