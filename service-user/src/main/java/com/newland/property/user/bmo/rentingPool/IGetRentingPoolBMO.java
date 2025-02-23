package com.newland.property.user.bmo.rentingPool;
import com.newland.property.dto.renting.RentingPoolDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingPoolBMO {


    /**
     * 查询房屋出租
     * add by wuxw
     * @param  rentingPoolDto
     * @return
     */
    ResponseEntity<String> get(RentingPoolDto rentingPoolDto);


}
