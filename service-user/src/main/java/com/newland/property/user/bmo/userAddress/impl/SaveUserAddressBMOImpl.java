package com.newland.property.user.bmo.userAddress.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IUserAddressInnerServiceSMO;
import com.newland.property.po.user.UserAddressPo;
import com.newland.property.user.bmo.userAddress.ISaveUserAddressBMO;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveUserAddressBMOImpl")
public class SaveUserAddressBMOImpl implements ISaveUserAddressBMO {

    @Autowired
    private IUserAddressInnerServiceSMO userAddressInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param userAddressPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(UserAddressPo userAddressPo) {
        int flag = 0;
        if (!StringUtil.isEmpty(userAddressPo.getAddressId()) && !userAddressPo.getAddressId().startsWith("-")) {
            flag = userAddressInnerServiceSMOImpl.updateUserAddress(userAddressPo);
        } else {
            userAddressPo.setAddressId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_addressId));
            flag = userAddressInnerServiceSMOImpl.saveUserAddress(userAddressPo);
        }
        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
