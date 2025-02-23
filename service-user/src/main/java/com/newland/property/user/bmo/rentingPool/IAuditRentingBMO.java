package com.newland.property.user.bmo.rentingPool;

import com.newland.property.po.renting.RentingPoolFlowPo;
import org.springframework.http.ResponseEntity;

public interface IAuditRentingBMO {

    /**
     * 审核
     * @param rentingPoolFlowPo
     * @return
     */
    public ResponseEntity<String> audit(RentingPoolFlowPo rentingPoolFlowPo,String userId);
}
