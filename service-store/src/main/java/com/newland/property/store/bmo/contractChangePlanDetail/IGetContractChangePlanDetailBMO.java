package com.newland.property.store.bmo.contractChangePlanDetail;
import com.newland.property.dto.contract.ContractChangePlanDetailDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractChangePlanDetailBMO {


    /**
     * 查询合同变更明细
     * add by wuxw
     * @param  contractChangePlanDetailDto
     * @return
     */
    ResponseEntity<String> get(ContractChangePlanDetailDto contractChangePlanDetailDto);


}
