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
package com.newland.property.job.adapt.hcIot.parkingArea;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.community.IParkingAreaInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.adapt.hcIot.asyn.IIotSendAsyn;
import com.newland.property.po.parking.ParkingAreaPo;
import com.newland.property.utils.constant.StatusConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HC iot 设备同步适配器
 * <p>
 * 接口协议地址： https://gitee.com/java110/MicroCommunityThings/blob/master/back/docs/api.md
 *
 * @desc add by 吴学文 18:58
 */
@Component(value = "deleteParkingAreaToIotAdapt")
public class DeleteParkingAreaToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IIotSendAsyn hcParkingAreaAsynImpl;

    @Autowired
    private IParkingAreaInnerServiceSMO parkingAreaInnerServiceSMOImpl;

    /**
     * {
     * "extParkingAreaId": "702020042194860037"
     * }
     *
     * @param business   当前处理业务
     * @param businesses 所有业务信息
     */
    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessParkingAreas = new JSONArray();
        if (data.containsKey(ParkingAreaPo.class.getSimpleName())) {
            Object bObj = data.get(ParkingAreaPo.class.getSimpleName());
            if (bObj instanceof JSONObject) {

                businessParkingAreas.add(bObj);
            } else if (bObj instanceof List) {
                businessParkingAreas = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessParkingAreas = (JSONArray) bObj;
            }
            //JSONObject businessParkingArea = data.getJSONObject("businessParkingArea");

        }else {
            if (data instanceof JSONObject) {

                businessParkingAreas.add(data);
            }
        }
        for (int bParkingAreaIndex = 0; bParkingAreaIndex < businessParkingAreas.size(); bParkingAreaIndex++) {
            JSONObject businessParkingArea = businessParkingAreas.getJSONObject(bParkingAreaIndex);
            doSendParkingArea(business, businessParkingArea);
        }
    }

    private void doSendParkingArea(Business business, JSONObject businessParkingArea) {
        ParkingAreaPo parkingAreaPo = BeanConvertUtil.covertBean(businessParkingArea, ParkingAreaPo.class);
        ParkingAreaDto parkingAreaDto = new ParkingAreaDto();
        parkingAreaDto.setPaId(parkingAreaPo.getPaId());
        parkingAreaDto.setStatusCd(StatusConstant.STATUS_CD_INVALID);
        List<ParkingAreaDto> parkingAreaDtos = parkingAreaInnerServiceSMOImpl.queryParkingAreas(parkingAreaDto);
        Assert.listOnlyOne(parkingAreaDtos, "未找到停车场");
        JSONObject postParameters = new JSONObject();
        postParameters.put("extPaId", parkingAreaDtos.get(0).getPaId());
        postParameters.put("extCommunityId", parkingAreaDtos.get(0).getCommunityId());
        postParameters.put("num", parkingAreaDtos.get(0).getNum());
        hcParkingAreaAsynImpl.deleteParkingArea(postParameters);
    }
}
