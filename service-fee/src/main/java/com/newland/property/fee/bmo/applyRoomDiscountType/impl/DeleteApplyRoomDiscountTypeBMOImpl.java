package com.newland.property.fee.bmo.applyRoomDiscountType.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.applyRoomDiscountType.IDeleteApplyRoomDiscountTypeBMO;
import com.newland.property.intf.fee.IApplyRoomDiscountTypeInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountTypePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteApplyRoomDiscountTypeBMOImpl")
public class DeleteApplyRoomDiscountTypeBMOImpl implements IDeleteApplyRoomDiscountTypeBMO {

    @Autowired
    private IApplyRoomDiscountTypeInnerServiceSMO applyRoomDiscountTypeInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountTypePo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ApplyRoomDiscountTypePo applyRoomDiscountTypePo) {

        int flag = applyRoomDiscountTypeInnerServiceSMOImpl.deleteApplyRoomDiscountType(applyRoomDiscountTypePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
