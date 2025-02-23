package com.newland.property.user.bmo.rentingPoolAttr;
import com.newland.property.po.renting.RentingPoolAttrPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRentingPoolAttrBMO {


    /**
     * 修改出租房屋属性
     * add by wuxw
     * @param rentingPoolAttrPo
     * @return
     */
    ResponseEntity<String> update(RentingPoolAttrPo rentingPoolAttrPo);


}
