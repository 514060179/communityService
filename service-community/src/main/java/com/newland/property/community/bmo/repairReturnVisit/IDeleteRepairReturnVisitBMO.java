package com.newland.property.community.bmo.repairReturnVisit;
import com.newland.property.po.repair.RepairReturnVisitPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteRepairReturnVisitBMO {


    /**
     * 修改报修回访
     * add by wuxw
     * @param repairReturnVisitPo
     * @return
     */
    ResponseEntity<String> delete(RepairReturnVisitPo repairReturnVisitPo);


}
