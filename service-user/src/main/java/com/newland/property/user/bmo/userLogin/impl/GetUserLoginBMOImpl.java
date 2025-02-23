package com.newland.property.user.bmo.userLogin.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.user.UserLoginDto;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserLoginInnerServiceSMO;
import com.newland.property.user.bmo.userLogin.IGetUserLoginBMO;
import com.newland.property.utils.cache.CommonCache;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserLoginBMOImpl")
public class GetUserLoginBMOImpl implements IGetUserLoginBMO {

    public static final String PREFIX_CODE = "newland_";

    @Autowired
    private IUserLoginInnerServiceSMO userLoginInnerServiceSMOImpl;


    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    /**
     * @param userLoginDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(UserLoginDto userLoginDto) {


        int count = userLoginInnerServiceSMOImpl.queryUserLoginsCount(userLoginDto);

        List<UserLoginDto> userLoginDtos = null;
        if (count > 0) {
            userLoginDtos = userLoginInnerServiceSMOImpl.queryUserLogins(userLoginDto);
        } else {
            userLoginDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) userLoginDto.getRow()), count, userLoginDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 生成HCCODE
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseEntity<String> generatorHcCode(UserDto userDto) {

        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        if(userDtos == null || userDtos.size()< 1){
            throw new IllegalArgumentException("用户不存在");
        }

        String hcCode = PREFIX_CODE + GenerateCodeFactory.getUUID();
        CommonCache.setValue(hcCode, JSONObject.toJSONString(userDtos.get(0)), CommonCache.defaultExpireTime);
        JSONObject paramOut = new JSONObject();
        paramOut.put("hcCode", hcCode);
        return ResultVo.createResponseEntity(paramOut);
    }

}
