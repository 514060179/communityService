package com.newland.property.user.bmo.rentingAppointment.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IRentingAppointmentInnerServiceSMO;
import com.newland.property.po.renting.RentingAppointmentPo;
import com.newland.property.user.bmo.rentingAppointment.IUpdateRentingAppointmentBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateRentingAppointmentBMOImpl")
public class UpdateRentingAppointmentBMOImpl implements IUpdateRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(RentingAppointmentPo rentingAppointmentPo) {

        int flag = rentingAppointmentInnerServiceSMOImpl.updateRentingAppointment(rentingAppointmentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
