package com.newland.property.store.bmo.contractChangePlanDetailAttr;

import com.newland.property.po.contract.ContractChangePlanDetailAttrPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractChangePlanDetailAttrBMO {


    /**
     * 添加合同变更属性
     * add by wuxw
     * @param contractChangePlanDetailAttrPo
     * @return
     */
    ResponseEntity<String> save(ContractChangePlanDetailAttrPo contractChangePlanDetailAttrPo);


}
