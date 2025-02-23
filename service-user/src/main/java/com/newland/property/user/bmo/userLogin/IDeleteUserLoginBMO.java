package com.newland.property.user.bmo.userLogin;
import com.newland.property.dto.user.UserDto;
import com.newland.property.po.user.UserLoginPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteUserLoginBMO {


    /**
     * 修改用户登录
     * add by wuxw
     * @param userLoginPo
     * @return
     */
    ResponseEntity<String> delete(UserLoginPo userLoginPo);
    /**
     * 修改用户登录
     * add by wuxw
     * @param userDto
     * @return
     */
    ResponseEntity<String> deleteOpenId(UserDto userDto);


}
