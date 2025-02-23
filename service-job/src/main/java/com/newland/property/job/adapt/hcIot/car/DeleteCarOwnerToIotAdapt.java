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
package com.newland.property.job.adapt.hcIot.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.file.FileDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.machine.MachineDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.common.IMachineInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.newland.property.po.car.OwnerCarPo;
import com.newland.property.utils.constant.StatusConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * HC iot 设备同步适配器
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "deleteCarOwnerToIotAdapt")
public class DeleteCarOwnerToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcOwnerCarAsynImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;
    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;
    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    /**
     * {
     * "extOwnerCarId": "702020042194860037"
     * }
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessOwnerCars = new JSONArray();
        if (data.containsKey(OwnerCarPo.class.getSimpleName())) {
            Object bObj = data.get(OwnerCarPo.class.getSimpleName());

            if (bObj instanceof JSONObject) {

                businessOwnerCars.add(bObj);
            } else if (bObj instanceof List) {
                businessOwnerCars = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessOwnerCars = (JSONArray) bObj;
            }

        } else {
            if (data instanceof JSONObject) {
                businessOwnerCars.add(data);
            }
        }

        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bOwnerCarIndex = 0; bOwnerCarIndex < businessOwnerCars.size(); bOwnerCarIndex++) {
            JSONObject businessOwnerCar = businessOwnerCars.getJSONObject(bOwnerCarIndex);
            doSendOwnerCar(business, businessOwnerCar);
        }
    }

    private void doSendOwnerCar(Business business, JSONObject businessOwnerCar) {
        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessOwnerCar, OwnerCarPo.class);
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarId(ownerCarPo.getCarId());
        ownerCarDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        ownerCarDto.setCarTypeCds(new String[]{OwnerCarDto.CAR_TYPE_PRIMARY,OwnerCarDto.CAR_TYPE_MEMBER});
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if(ownerCarDtos == null || ownerCarDtos.size() < 1){
            throw new IllegalArgumentException("未找到车辆");
        }

        //没有车位就不同步了
        if (StringUtil.isEmpty(ownerCarDtos.get(0).getPsId()) || "-1".equals(ownerCarDtos.get(0).getPsId())) {
            return;
        }


        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(ownerCarDtos.get(0).getOwnerId());
        ownerDto.setCommunityId(ownerCarDtos.get(0).getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        Assert.listOnlyOne(ownerDtos, "业主不存在");

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
        parkingSpaceDto.setCommunityId(ownerCarDtos.get(0).getCommunityId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        Assert.listOnlyOne(ownerCarDtos, "未找到车位");


        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(ownerCarDtos.get(0).getOwnerId());
        fileRelDto.setRelTypeCd("10000");
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos == null || fileRelDtos.size() != 1) {
            return;
        }
        FileDto fileDto = new FileDto();
        fileDto.setFileId(fileRelDtos.get(0).getFileSaveName());
        fileDto.setFileSaveName(fileRelDtos.get(0).getFileSaveName());
        fileDto.setCommunityId(ownerCarDtos.get(0).getCommunityId());
        List<FileDto> fileDtos = fileInnerServiceSMOImpl.queryFiles(fileDto);
        if (fileDtos == null || fileDtos.size() != 1) {
            return;
        }
        //根据小区ID查询现有设备
        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(ownerCarDtos.get(0).getCommunityId());
        List<String> locationObjIds = new ArrayList<>();
        locationObjIds.add(parkingSpaceDtos.get(0).getPaId());

        machineDto.setMachineTypeCd(MachineDto.MACHINE_TYPE_ACCESS_CONTROL);
        machineDto.setLocationObjIds(locationObjIds.toArray(new String[locationObjIds.size()]));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);
        //没有设备
        if (machineDtos == null || machineDtos.size() < 1) {
            return;
        }
        for (MachineDto tmpMachineDto : machineDtos) {
            if (!"9999".equals(tmpMachineDto.getMachineTypeCd())) {
                continue;
            }
            JSONObject postParameters = new JSONObject();

            postParameters.put("machineCode", tmpMachineDto.getMachineCode());
            postParameters.put("userId", ownerDtos.get(0).getMemberId());
            postParameters.put("name", ownerDtos.get(0).getName());
            postParameters.put("extMachineId", tmpMachineDto.getMachineId());
            postParameters.put("extCommunityId", tmpMachineDto.getCommunityId());
            hcOwnerCarAsynImpl.sendUpdateOwner(postParameters);
        }
    }
}
