package com.newland.property.user.bmo.rentingPoolAttr;

import com.newland.property.po.renting.RentingPoolAttrPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingPoolAttrBMO {


    /**
     * 添加出租房屋属性
     * add by wuxw
     * @param rentingPoolAttrPo
     * @return
     */
    ResponseEntity<String> save(RentingPoolAttrPo rentingPoolAttrPo);


}
