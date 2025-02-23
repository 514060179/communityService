package com.newland.property.community.bmo.repairReturnVisit;

import com.newland.property.po.repair.RepairReturnVisitPo;
import org.springframework.http.ResponseEntity;

public interface ISaveRepairReturnVisitBMO {


    /**
     * 添加报修回访
     * add by wuxw
     *
     * @param repairReturnVisitPo
     * @return
     */
    ResponseEntity<String> save(RepairReturnVisitPo repairReturnVisitPo);


}
