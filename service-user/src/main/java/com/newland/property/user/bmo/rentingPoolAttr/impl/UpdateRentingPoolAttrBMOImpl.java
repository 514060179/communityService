package com.newland.property.user.bmo.rentingPoolAttr.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IRentingPoolAttrInnerServiceSMO;
import com.newland.property.po.renting.RentingPoolAttrPo;
import com.newland.property.user.bmo.rentingPoolAttr.IUpdateRentingPoolAttrBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingPoolAttrBMOImpl")
public class UpdateRentingPoolAttrBMOImpl implements IUpdateRentingPoolAttrBMO {

    @Autowired
    private IRentingPoolAttrInnerServiceSMO rentingPoolAttrInnerServiceSMOImpl;

    /**
     * @param rentingPoolAttrPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(RentingPoolAttrPo rentingPoolAttrPo) {

        int flag = rentingPoolAttrInnerServiceSMOImpl.updateRentingPoolAttr(rentingPoolAttrPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
