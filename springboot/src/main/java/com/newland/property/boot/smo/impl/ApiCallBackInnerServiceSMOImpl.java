package com.newland.property.boot.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.boot.websocket.ParkingAreaWebsocket;
import com.newland.property.boot.websocket.ParkingBoxWebsocket;
import com.newland.property.dto.parking.ParkingBoxAreaDto;
import com.newland.property.intf.api.IApiCallBackInnerServiceSMO;
import com.newland.property.intf.community.IParkingBoxAreaV1InnerServiceSMO;
import com.newland.property.utils.exception.SMOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiCallBackInnerServiceSMOImpl implements IApiCallBackInnerServiceSMO {

    @Autowired
    private IParkingBoxAreaV1InnerServiceSMO parkingBoxAreaV1InnerServiceSMOImpl;

    @Override
    public int webSentParkingArea(@RequestBody JSONObject reqJson) {
        JSONObject param = JSONObject.parseObject(reqJson.toString());
        try {
            ParkingBoxWebsocket.sendInfo(param.toJSONString(), param.getString("extBoxId"));

        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }

        ParkingBoxAreaDto parkingBoxAreaDto = new ParkingBoxAreaDto();
        parkingBoxAreaDto.setBoxId(reqJson.getString("extBoxId"));
        parkingBoxAreaDto.setDefaultArea(ParkingBoxAreaDto.DEFAULT_AREA_TRUE);

        List<ParkingBoxAreaDto> parkingBoxAreaDtos = parkingBoxAreaV1InnerServiceSMOImpl.queryParkingBoxAreas(parkingBoxAreaDto);

        if(parkingBoxAreaDtos == null || parkingBoxAreaDtos.size()<1){
            return 1;
        }

        try {
            ParkingAreaWebsocket.sendInfo(param.toJSONString(), parkingBoxAreaDtos.get(0).getPaId());
        } catch (Exception e) {
            throw new SMOException(e.getMessage());
        }
        return 1;
    }
}
