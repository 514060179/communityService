package com.newland.property.user.bmo.rentingPool;

import com.alibaba.fastjson.JSONArray;
import com.newland.property.po.renting.RentingPoolPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingPoolBMO {


    /**
     * 添加房屋出租
     * add by wuxw
     * @param rentingPoolPo
     * @return
     */
    ResponseEntity<String> save(RentingPoolPo rentingPoolPo, JSONArray photos);


}
