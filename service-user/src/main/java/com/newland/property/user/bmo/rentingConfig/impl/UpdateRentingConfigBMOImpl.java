package com.newland.property.user.bmo.rentingConfig.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IRentingConfigInnerServiceSMO;
import com.newland.property.po.renting.RentingConfigPo;
import com.newland.property.user.bmo.rentingConfig.IUpdateRentingConfigBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingConfigBMOImpl")
public class UpdateRentingConfigBMOImpl implements IUpdateRentingConfigBMO {

    @Autowired
    private IRentingConfigInnerServiceSMO rentingConfigInnerServiceSMOImpl;

    /**
     * @param rentingConfigPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(RentingConfigPo rentingConfigPo) {

        int flag = rentingConfigInnerServiceSMOImpl.updateRentingConfig(rentingConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
