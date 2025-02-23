package com.newland.property.community.bmo.roomRenovation.impl;

import com.newland.property.community.bmo.roomRenovation.IGetRoomRenovationBMO;
import com.newland.property.dto.room.RoomRenovationDto;
import com.newland.property.intf.community.IRoomRenovationInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service("getRoomRenovationBMOImpl")
public class GetRoomRenovationBMOImpl implements IGetRoomRenovationBMO {

    @Autowired
    private IRoomRenovationInnerServiceSMO roomRenovationInnerServiceSMOImpl;

    /**
     * @param roomRenovationDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(@RequestBody RoomRenovationDto roomRenovationDto) {


        int count = roomRenovationInnerServiceSMOImpl.queryRoomRenovationsCount(roomRenovationDto);

        List<RoomRenovationDto> roomRenovationDtos = new ArrayList<>();
        if (count > 0) {
            List<RoomRenovationDto> roomRenovationDtoList = roomRenovationInnerServiceSMOImpl.queryRoomRenovations(roomRenovationDto);
            for (RoomRenovationDto renovationDto : roomRenovationDtoList) {
                renovationDto.setUserId(roomRenovationDto.getUserId());
                roomRenovationDtos.add(renovationDto);
            }
        } else {
            roomRenovationDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) roomRenovationDto.getRow()), count, roomRenovationDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
