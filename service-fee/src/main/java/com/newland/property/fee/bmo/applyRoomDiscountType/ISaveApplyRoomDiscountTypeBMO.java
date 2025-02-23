package com.newland.property.fee.bmo.applyRoomDiscountType;

import com.newland.property.po.room.ApplyRoomDiscountTypePo;
import org.springframework.http.ResponseEntity;
public interface ISaveApplyRoomDiscountTypeBMO {


    /**
     * 添加优惠申请类型
     * add by wuxw
     * @param applyRoomDiscountTypePo
     * @return
     */
    ResponseEntity<String> save(ApplyRoomDiscountTypePo applyRoomDiscountTypePo);


}
