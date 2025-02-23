package com.newland.property.user.bmo.rentingAppointment;
import com.newland.property.po.renting.RentingAppointmentPo;
import org.springframework.http.ResponseEntity;

public interface IConfirmRentingBMO {


    /**
     * 确认出租
     * add by wuxw
     * @param rentingAppointmentPo
     * @return
     */
    ResponseEntity<String> confirm(RentingAppointmentPo rentingAppointmentPo);


}
