package com.newland.property.user.bmo.rentingConfig;

import com.newland.property.po.renting.RentingConfigPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingConfigBMO {


    /**
     * 添加房屋出租配置
     * add by wuxw
     * @param rentingConfigPo
     * @return
     */
    ResponseEntity<String> save(RentingConfigPo rentingConfigPo);


}
