package com.newland.property.store.bmo.contractChangePlanDetailAttr;
import com.newland.property.po.contract.ContractChangePlanDetailAttrPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractChangePlanDetailAttrBMO {


    /**
     * 修改合同变更属性
     * add by wuxw
     * @param contractChangePlanDetailAttrPo
     * @return
     */
    ResponseEntity<String> update(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);


}
