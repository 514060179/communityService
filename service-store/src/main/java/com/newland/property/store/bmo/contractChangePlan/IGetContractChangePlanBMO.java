package com.newland.property.store.bmo.contractChangePlan;
import com.newland.property.dto.contract.ContractChangePlanDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractChangePlanBMO {


    /**
     * 查询合同变更计划
     * add by wuxw
     * @param  contractChangePlanDto
     * @return
     */
    ResponseEntity<String> get(ContractChangePlanDto contractChangePlanDto);


}
