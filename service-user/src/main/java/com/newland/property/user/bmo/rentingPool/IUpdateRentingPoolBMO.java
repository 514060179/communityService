package com.newland.property.user.bmo.rentingPool;

import com.newland.property.po.renting.RentingPoolPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRentingPoolBMO {


    /**
     * 修改房屋出租
     * add by wuxw
     *
     * @param rentingPoolPo
     * @return
     */
    ResponseEntity<String> update(RentingPoolPo rentingPoolPo);


}
