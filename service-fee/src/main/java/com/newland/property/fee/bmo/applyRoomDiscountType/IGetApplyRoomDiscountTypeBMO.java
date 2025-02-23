package com.newland.property.fee.bmo.applyRoomDiscountType;
import com.newland.property.dto.room.ApplyRoomDiscountTypeDto;
import org.springframework.http.ResponseEntity;
public interface IGetApplyRoomDiscountTypeBMO {


    /**
     * 查询优惠申请类型
     * add by wuxw
     * @param  applyRoomDiscountTypeDto
     * @return
     */
    ResponseEntity<String> get(ApplyRoomDiscountTypeDto applyRoomDiscountTypeDto);


}
