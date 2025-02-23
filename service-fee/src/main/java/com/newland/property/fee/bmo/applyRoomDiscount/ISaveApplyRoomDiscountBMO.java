package com.newland.property.fee.bmo.applyRoomDiscount;

import com.newland.property.po.room.ApplyRoomDiscountPo;
import org.springframework.http.ResponseEntity;
public interface ISaveApplyRoomDiscountBMO {


    /**
     * 添加房屋折扣申请
     * add by wuxw
     * @param applyRoomDiscountPo
     * @return
     */
    ResponseEntity<String> save(ApplyRoomDiscountPo applyRoomDiscountPo);


}
