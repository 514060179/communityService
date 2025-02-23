package com.newland.property.user.bmo.rentingAppointment.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IRentingAppointmentInnerServiceSMO;
import com.newland.property.po.renting.RentingAppointmentPo;
import com.newland.property.user.bmo.rentingAppointment.IDeleteRentingAppointmentBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRentingAppointmentBMOImpl")
public class DeleteRentingAppointmentBMOImpl implements IDeleteRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(RentingAppointmentPo rentingAppointmentPo) {

        int flag = rentingAppointmentInnerServiceSMOImpl.deleteRentingAppointment(rentingAppointmentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
