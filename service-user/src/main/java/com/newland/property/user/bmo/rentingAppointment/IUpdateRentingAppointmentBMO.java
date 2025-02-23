package com.newland.property.user.bmo.rentingAppointment;
import com.newland.property.po.renting.RentingAppointmentPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateRentingAppointmentBMO {


    /**
     * 修改租赁预约
     * add by wuxw
     * @param rentingAppointmentPo
     * @return
     */
    ResponseEntity<String> update(RentingAppointmentPo rentingAppointmentPo);


}
