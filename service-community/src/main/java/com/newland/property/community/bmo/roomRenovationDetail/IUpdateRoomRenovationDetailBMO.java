package com.newland.property.community.bmo.roomRenovationDetail;

import com.newland.property.po.room.RoomRenovationDetailPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRoomRenovationDetailBMO {


    /**
     * 修改装修明细
     * add by wuxw
     *
     * @param roomRenovationDetailPo
     * @return
     */
    ResponseEntity<String> update(RoomRenovationDetailPo roomRenovationDetailPo);


}
