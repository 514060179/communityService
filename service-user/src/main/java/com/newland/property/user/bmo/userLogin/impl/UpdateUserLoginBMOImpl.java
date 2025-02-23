package com.newland.property.user.bmo.userLogin.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IUserLoginInnerServiceSMO;
import com.newland.property.po.user.UserLoginPo;
import com.newland.property.user.bmo.userLogin.IUpdateUserLoginBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateUserLoginBMOImpl")
public class UpdateUserLoginBMOImpl implements IUpdateUserLoginBMO {

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    /**
     * @param userLoginPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(UserLoginPo userLoginPo) {

        int flag = userLoginInnerServiceSMOImpl.updateUserLogin(userLoginPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
