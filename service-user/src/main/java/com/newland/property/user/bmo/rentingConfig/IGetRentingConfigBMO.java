package com.newland.property.user.bmo.rentingConfig;

import com.newland.property.dto.renting.RentingConfigDto;
import org.springframework.http.ResponseEntity;

public interface IGetRentingConfigBMO {


    /**
     * 查询房屋出租配置
     * add by wuxw
     *
     * @param rentingConfigDto
     * @return
     */
    ResponseEntity<String> get(RentingConfigDto rentingConfigDto);


}
