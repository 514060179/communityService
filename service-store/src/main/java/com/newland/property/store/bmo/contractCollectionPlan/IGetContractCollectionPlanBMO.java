package com.newland.property.store.bmo.contractCollectionPlan;
import com.newland.property.dto.contract.ContractCollectionPlanDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractCollectionPlanBMO {


    /**
     * 查询合同收款计划
     * add by wuxw
     * @param  contractCollectionPlanDto
     * @return
     */
    ResponseEntity<String> get(ContractCollectionPlanDto contractCollectionPlanDto);


}
