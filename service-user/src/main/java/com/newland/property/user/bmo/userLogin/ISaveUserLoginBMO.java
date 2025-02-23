package com.newland.property.user.bmo.userLogin;

import com.newland.property.po.user.UserLoginPo;
import org.springframework.http.ResponseEntity;
public interface ISaveUserLoginBMO {


    /**
     * 添加用户登录
     * add by wuxw
     * @param userLoginPo
     * @return
     */
    ResponseEntity<String> save(UserLoginPo userLoginPo);


}
