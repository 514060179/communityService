package com.newland.property.user.bmo.rentingAppointment.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.renting.RentingAppointmentDto;
import com.newland.property.intf.user.IRentingAppointmentInnerServiceSMO;
import com.newland.property.po.renting.RentingAppointmentPo;
import com.newland.property.user.bmo.rentingAppointment.ISaveRentingAppointmentBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingAppointmentBMOImpl")
public class SaveRentingAppointmentBMOImpl implements ISaveRentingAppointmentBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingAppointmentPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(RentingAppointmentPo rentingAppointmentPo) {

        rentingAppointmentPo.setAppointmentId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_appointmentId));
        rentingAppointmentPo.setState(RentingAppointmentDto.STATE_SUBMIT);
        int flag = rentingAppointmentInnerServiceSMOImpl.saveRentingAppointment(rentingAppointmentPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
