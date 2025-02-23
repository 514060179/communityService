package com.newland.property.user.bmo.rentingConfig.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IRentingConfigInnerServiceSMO;
import com.newland.property.po.renting.RentingConfigPo;
import com.newland.property.user.bmo.rentingConfig.ISaveRentingConfigBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingConfigBMOImpl")
public class SaveRentingConfigBMOImpl implements ISaveRentingConfigBMO {

    @Autowired
    private IRentingConfigInnerServiceSMO rentingConfigInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingConfigPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(RentingConfigPo rentingConfigPo) {

        rentingConfigPo.setRentingConfigId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_rentingConfigId));
        int flag = rentingConfigInnerServiceSMOImpl.saveRentingConfig(rentingConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
