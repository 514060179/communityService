package com.newland.property.user.bmo.userLogin;
import com.newland.property.po.user.UserLoginPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateUserLoginBMO {


    /**
     * 修改用户登录
     * add by wuxw
     * @param userLoginPo
     * @return
     */
    ResponseEntity<String> update(UserLoginPo userLoginPo);


}
