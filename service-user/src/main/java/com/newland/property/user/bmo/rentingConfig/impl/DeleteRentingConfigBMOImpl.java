package com.newland.property.user.bmo.rentingConfig.impl;

import com.newland.property.core.annotation.PropertyTransactional;

import com.newland.property.intf.user.IRentingConfigInnerServiceSMO;
import com.newland.property.po.renting.RentingConfigPo;
import com.newland.property.user.bmo.rentingConfig.IDeleteRentingConfigBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRentingConfigBMOImpl")
public class DeleteRentingConfigBMOImpl implements IDeleteRentingConfigBMO {

    @Autowired
    private IRentingConfigInnerServiceSMO rentingConfigInnerServiceSMOImpl;

    /**
     * @param rentingConfigPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(RentingConfigPo rentingConfigPo) {

        int flag = rentingConfigInnerServiceSMOImpl.deleteRentingConfig(rentingConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
