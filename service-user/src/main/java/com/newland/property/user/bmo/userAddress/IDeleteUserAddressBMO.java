package com.newland.property.user.bmo.userAddress;
import com.newland.property.po.user.UserAddressPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteUserAddressBMO {


    /**
     * 修改用户联系地址
     * add by wuxw
     * @param userAddressPo
     * @return
     */
    ResponseEntity<String> delete(UserAddressPo userAddressPo);


}
