package com.newland.property.user.bmo.rentingAppointment;

import com.newland.property.po.renting.RentingAppointmentPo;
import org.springframework.http.ResponseEntity;
public interface ISaveRentingAppointmentBMO {


    /**
     * 添加租赁预约
     * add by wuxw
     * @param rentingAppointmentPo
     * @return
     */
    ResponseEntity<String> save(RentingAppointmentPo rentingAppointmentPo);


}
