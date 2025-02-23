package com.newland.property.community.bmo.roomRenovation;
import com.newland.property.po.room.RoomRenovationPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteRoomRenovationBMO {


    /**
     * 修改装修申请
     * add by wuxw
     * @param roomRenovationPo
     * @return
     */
    ResponseEntity<String> delete(RoomRenovationPo roomRenovationPo);


}
