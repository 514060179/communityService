package com.newland.property.store.bmo.contractChangePlanDetail;
import com.newland.property.po.contract.ContractChangePlanDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractChangePlanDetailBMO {


    /**
     * 修改合同变更明细
     * add by wuxw
     * @param contractChangePlanDetailPo
     * @return
     */
    ResponseEntity<String> delete(ContractChangePlanDetailPo contractChangePlanDetailPo);


}
