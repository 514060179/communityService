package com.newland.property.community.bmo.applyRoomDiscountRecord;

import com.newland.property.po.room.ApplyRoomDiscountRecordPo;
import org.springframework.http.ResponseEntity;

/**
 * 添加空置房验房记录
 *
 * @author fqz
 * @date 2021-09-01
 */
public interface ISaveApplyRoomDiscountRecordBMO {

    /**
     * 添加空置房验房记录
     *
     * @param applyRoomDiscountRecordPo
     * @return
     */
    ResponseEntity<String> saveRecord(ApplyRoomDiscountRecordPo applyRoomDiscountRecordPo);

}
