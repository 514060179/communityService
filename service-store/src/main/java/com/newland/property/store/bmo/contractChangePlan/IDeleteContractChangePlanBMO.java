package com.newland.property.store.bmo.contractChangePlan;
import com.newland.property.po.contract.ContractChangePlanPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractChangePlanBMO {


    /**
     * 修改合同变更计划
     * add by wuxw
     * @param contractChangePlanPo
     * @return
     */
    ResponseEntity<String> delete(ContractChangePlanPo contractChangePlanPo);


}
