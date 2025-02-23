package com.newland.property.user.bmo.userAddress;
import com.newland.property.dto.user.UserAddressDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserAddressBMO {


    /**
     * 查询用户联系地址
     * add by wuxw
     * @param  userAddressDto
     * @return
     */
    ResponseEntity<String> get(UserAddressDto userAddressDto);


}
