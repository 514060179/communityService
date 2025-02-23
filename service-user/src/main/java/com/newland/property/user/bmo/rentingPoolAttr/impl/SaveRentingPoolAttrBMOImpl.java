package com.newland.property.user.bmo.rentingPoolAttr.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IRentingPoolAttrInnerServiceSMO;
import com.newland.property.po.renting.RentingPoolAttrPo;
import com.newland.property.user.bmo.rentingPoolAttr.ISaveRentingPoolAttrBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingPoolAttrBMOImpl")
public class SaveRentingPoolAttrBMOImpl implements ISaveRentingPoolAttrBMO {

    @Autowired
    private IRentingPoolAttrInnerServiceSMO rentingPoolAttrInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingPoolAttrPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(RentingPoolAttrPo rentingPoolAttrPo) {

        rentingPoolAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
        int flag = rentingPoolAttrInnerServiceSMOImpl.saveRentingPoolAttr(rentingPoolAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
