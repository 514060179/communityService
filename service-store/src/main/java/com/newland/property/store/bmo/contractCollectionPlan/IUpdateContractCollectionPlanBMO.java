package com.newland.property.store.bmo.contractCollectionPlan;
import com.newland.property.po.contract.ContractCollectionPlanPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractCollectionPlanBMO {


    /**
     * 修改合同收款计划
     * add by wuxw
     * @param contractCollectionPlanPo
     * @return
     */
    ResponseEntity<String> update(ContractCollectionPlanPo contractCollectionPlanPo);


}
