package com.newland.property.community.bmo.roomRenovation;
import com.newland.property.dto.room.RoomRenovationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IGetRoomRenovationBMO {


    /**
     * 查询装修申请
     * add by wuxw
     * @param  roomRenovationDto
     * @return
     */
    ResponseEntity<String> get(@RequestBody RoomRenovationDto roomRenovationDto);


}
