package com.newland.property.user.bmo.rentingPoolAttr;
import com.newland.property.dto.renting.RentingPoolAttrDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingPoolAttrBMO {


    /**
     * 查询出租房屋属性
     * add by wuxw
     * @param  rentingPoolAttrDto
     * @return
     */
    ResponseEntity<String> get(RentingPoolAttrDto rentingPoolAttrDto);


}
