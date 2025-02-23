package com.newland.property.community.bmo.roomRenovationRecord;

import com.newland.property.po.room.RoomRenovationRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 删除装修记录
 *
 * @author fqz
 * @date 2021-02-27 11:49
 */
public interface IDeleteRoomRenovationRecordBMO {

    /**
     * 删除装修记录
     *
     * @param roomRenovationRecordPo
     * @return
     */
    ResponseEntity<String> delete(RoomRenovationRecordPo roomRenovationRecordPo);

}
