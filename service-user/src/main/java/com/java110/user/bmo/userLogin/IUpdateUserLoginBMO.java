package com.java110.user.bmo.userLogin;
import com.java110.po.user.UserLoginPo;
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