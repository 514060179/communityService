package com.newland.property.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.community.bmo.assets.IQueryAssetsRoomBMO;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryAssetsRoomBMOImpl implements IQueryAssetsRoomBMO {


    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;


    /**
     * @param communityId
     * @return {
     * data:{
     * floorCount:30,
     * roomCount:29,
     * parkingSpaceCount:12,
     * machineCount:12
     * }
     * }
     */
    @Override
    public ResponseEntity<String> query(String communityId) {

        RoomDto roomDto = new RoomDto();
        roomDto.setCommunityId(communityId);
        roomDto.setState(RoomDto.STATE_SELL);
        int sellRoomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);

        roomDto.setState(RoomDto.STATE_FREE);
        int freeRoomCount = roomInnerServiceSMOImpl.queryRoomsCount(roomDto);
        JSONObject data = new JSONObject();
        data.put("sellRoomCount", sellRoomCount);
        data.put("freeRoomCount", freeRoomCount);
        return ResultVo.createResponseEntity(data);
    }
}
