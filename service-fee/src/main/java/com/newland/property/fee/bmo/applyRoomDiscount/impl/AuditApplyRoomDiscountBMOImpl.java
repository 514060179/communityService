package com.newland.property.fee.bmo.applyRoomDiscount.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.applyRoomDiscount.IAuditApplyRoomDiscountBMO;
import com.newland.property.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("auditApplyRoomDiscountBMOImpl")
public class AuditApplyRoomDiscountBMOImpl implements IAuditApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> audit(ApplyRoomDiscountPo applyRoomDiscountPo) {

        int flag = applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
