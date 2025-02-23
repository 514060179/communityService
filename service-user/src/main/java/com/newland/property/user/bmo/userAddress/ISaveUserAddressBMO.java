package com.newland.property.user.bmo.userAddress;

import com.newland.property.po.user.UserAddressPo;
import org.springframework.http.ResponseEntity;
public interface ISaveUserAddressBMO {


    /**
     * 添加用户联系地址
     * add by wuxw
     * @param userAddressPo
     * @return
     */
    ResponseEntity<String> save(UserAddressPo userAddressPo);


}
