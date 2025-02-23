package com.newland.property.community.bmo.repairReturnVisit;
import com.newland.property.dto.repair.RepairReturnVisitDto;
import org.springframework.http.ResponseEntity;
public interface IGetRepairReturnVisitBMO {


    /**
     * 查询报修回访
     * add by wuxw
     * @param  repairReturnVisitDto
     * @return
     */
    ResponseEntity<String> get(RepairReturnVisitDto repairReturnVisitDto);


}
