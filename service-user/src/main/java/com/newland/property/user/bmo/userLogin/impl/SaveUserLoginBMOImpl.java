package com.newland.property.user.bmo.userLogin.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IUserLoginInnerServiceSMO;
import com.newland.property.po.user.UserLoginPo;
import com.newland.property.user.bmo.userLogin.ISaveUserLoginBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveUserLoginBMOImpl")
public class SaveUserLoginBMOImpl implements ISaveUserLoginBMO {

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param userLoginPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(UserLoginPo userLoginPo) {

        userLoginPo.setLoginId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_loginId));
        int flag = userLoginInnerServiceSMOImpl.saveUserLogin(userLoginPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
