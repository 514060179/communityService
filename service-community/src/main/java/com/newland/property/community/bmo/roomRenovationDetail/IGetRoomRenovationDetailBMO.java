package com.newland.property.community.bmo.roomRenovationDetail;
import com.newland.property.dto.room.RoomRenovationDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetRoomRenovationDetailBMO {


    /**
     * 查询装修明细
     * add by wuxw
     * @param  roomRenovationDetailDto
     * @return
     */
    ResponseEntity<String> get(RoomRenovationDetailDto roomRenovationDetailDto);


}
