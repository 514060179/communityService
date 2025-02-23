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
package com.newland.property.job.adapt.hcGov.car;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.community.CommunityAttrDto;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.dto.parking.ParkingAreaAttrDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.adapt.hcGov.HcGovConstant;
import com.newland.property.job.adapt.hcGov.asyn.BaseHcGovSendAsyn;
import com.newland.property.po.car.OwnerCarPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增车辆同步HC政务接口
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "addCarToHcGovAdapt")
public class AddCarToHcGovAdapt extends DatabusAdaptImpl {

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;
    @Autowired
    private BaseHcGovSendAsyn baseHcGovSendAsynImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;


    /**
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
        }else {
            if (data instanceof JSONObject) {
                businessOwnerCars.add(data);
            }
        }
        //JSONObject businessOwnerCar = data.getJSONObject("businessOwnerCar");
        for (int bOwnerCarIndex = 0; bOwnerCarIndex < businessOwnerCars.size(); bOwnerCarIndex++) {
            JSONObject businessOwnerCar = businessOwnerCars.getJSONObject(bOwnerCarIndex);
            doAddOwnerCar(business, businessOwnerCar);
        }
    }

    private void doAddOwnerCar(Business business, JSONObject businessFloor) {

        OwnerCarPo ownerCarPo = BeanConvertUtil.covertBean(businessFloor, OwnerCarPo.class);

        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(ownerCarPo.getCommunityId());
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listNotNull(communityDtos, "未包含小区信息");
        CommunityDto tmpCommunityDto = communityDtos.get(0);
        String extCommunityId = "";
        String communityId = tmpCommunityDto.getCommunityId();
        if (tmpCommunityDto.getCommunityAttrDtos() != null && tmpCommunityDto.getCommunityAttrDtos().size() > 0) {
            for (CommunityAttrDto communityAttrDto : tmpCommunityDto.getCommunityAttrDtos()) {
                if (HcGovConstant.EXT_COMMUNITY_ID.equals(communityAttrDto.getSpecCd())) {
                    extCommunityId = communityAttrDto.getValue();
                }
            }
        }
        //查询车辆
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCommunityId(ownerCarPo.getCommunityId());
        ownerCarDto.setCarId(ownerCarPo.getCarId());
        List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        Assert.listOnlyOne(ownerCarDtos, "车辆不存在");

        //查询外部停车场
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setPsId(ownerCarDtos.get(0).getPsId());
        List<ParkingSpaceDto> parkingSpaceDtos = parkingSpaceInnerServiceSMOImpl.queryParkingSpaces(parkingSpaceDto);
        Assert.listOnlyOne(parkingSpaceDtos, "车位不存在");

        //查询 外部停车
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(parkingSpaceDtos.get(0).getPaId());
        parkingAreaDto.setCommunityId(parkingSpaceDtos.get(0).getCommunityId());
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listOnlyOne(parkingAreaDtos, "停车场不存在");

        String extPaId = getExtPaId(parkingAreaDtos.get(0).getAttrs());

        if (StringUtil.isEmpty(extPaId)) {
            return;
        }


        JSONObject body = new JSONObject();
        body.put("carNum", ownerCarDtos.get(0).getCarNum());
        body.put("carBrand", ownerCarDtos.get(0).getCarBrand());
        body.put("carType", ownerCarDtos.get(0).getCarType());
        body.put("carColor", ownerCarDtos.get(0).getCarColor());
        body.put("startTime", DateUtil.getFormatTimeString(ownerCarDtos.get(0).getStartTime(), DateUtil.DATE_FORMATE_STRING_A));
        body.put("endTime", DateUtil.getFormatTimeString(ownerCarDtos.get(0).getEndTime(), DateUtil.DATE_FORMATE_STRING_A));
        body.put("personName", ownerCarDtos.get(0).getOwnerName());
        body.put("personTel", ownerCarDtos.get(0).getLink());
        body.put("extPaId", extPaId);

        JSONObject kafkaData = baseHcGovSendAsynImpl.createHeadersOrBody(body, extCommunityId, HcGovConstant.ADD_CAR_ACTION, HcGovConstant.COMMUNITY_SECURE);
        baseHcGovSendAsynImpl.sendKafka(HcGovConstant.GOV_TOPIC, kafkaData, communityId, ownerCarPo.getCarId(), HcGovConstant.COMMUNITY_SECURE);
    }

    private String getExtPaId(List<ParkingAreaAttrDto> attrs) {

        if (attrs == null || attrs.size() < 1) {
            return "";
        }

        for (ParkingAreaAttrDto parkingAreaAttrDto : attrs) {
            if (parkingAreaAttrDto.getSpecCd().equals(HcGovConstant.EXT_COMMUNITY_ID)) {
                return parkingAreaAttrDto.getValue();
            }
        }
        return "";
    }

}
