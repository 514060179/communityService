package com.newland.property.community.bmo.park.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.community.bmo.park.IQueryParkSpace;
import com.newland.property.dto.parking.ParkingSpaceDto;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryParkSpaceImpl implements IQueryParkSpace {

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> query(JSONObject reqJson) {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setCommunityId(reqJson.getString("code"));
        int total = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        parkingSpaceDto.setState(ParkingSpaceDto.STATE_FREE);
        int freeTotal = parkingSpaceInnerServiceSMOImpl.queryParkingSpacesCount(parkingSpaceDto);
        JSONObject data = new JSONObject();
        data.put("total", total);
        data.put("last", freeTotal);
        return ResultVo.createResponseEntity(1, 1, data);
    }
}
