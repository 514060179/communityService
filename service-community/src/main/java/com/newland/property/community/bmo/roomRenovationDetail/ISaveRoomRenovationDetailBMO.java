package com.newland.property.community.bmo.roomRenovationDetail;

import com.newland.property.po.room.RoomRenovationDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRoomRenovationDetailBMO {


    /**
     * 添加装修明细
     * add by wuxw
     * @param roomRenovationDetailPo
     * @return
     */
    ResponseEntity<String> save(RoomRenovationDetailPo roomRenovationDetailPo);


}
