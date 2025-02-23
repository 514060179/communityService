package com.newland.property.user.bmo.rentingPoolFlow;
import com.newland.property.dto.renting.RentingPoolFlowDto;
import org.springframework.http.ResponseEntity;
public interface IGetRentingPoolFlowBMO {


    /**
     * 查询出租流程
     * add by wuxw
     * @param  rentingPoolFlowDto
     * @return
     */
    ResponseEntity<String> get(RentingPoolFlowDto rentingPoolFlowDto);


}
