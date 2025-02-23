package com.newland.property.fee.bmo.applyRoomDiscount;
import com.newland.property.dto.room.ApplyRoomDiscountDto;
import org.springframework.http.ResponseEntity;
public interface IGetApplyRoomDiscountBMO {


    /**
     * 查询房屋折扣申请
     * add by wuxw
     * @param  applyRoomDiscountDto
     * @return
     */
    ResponseEntity<String> get(ApplyRoomDiscountDto applyRoomDiscountDto);


}
