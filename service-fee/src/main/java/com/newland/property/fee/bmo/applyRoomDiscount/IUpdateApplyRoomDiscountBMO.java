package com.newland.property.fee.bmo.applyRoomDiscount;

import com.newland.property.po.room.ApplyRoomDiscountPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateApplyRoomDiscountBMO {


    /**
     * 修改房屋折扣申请
     * add by wuxw
     *
     * @param applyRoomDiscountPo
     * @return
     */
    ResponseEntity<String> update(ApplyRoomDiscountPo applyRoomDiscountPo);


}
