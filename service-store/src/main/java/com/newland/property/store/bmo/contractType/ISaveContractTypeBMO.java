package com.newland.property.store.bmo.contractType;

import com.newland.property.po.contract.ContractTypePo;
import org.springframework.http.ResponseEntity;

public interface ISaveContractTypeBMO {


    /**
     * 添加合同类型
     * add by wuxw
     *
     * @param contractTypePo
     * @return
     */
    ResponseEntity<String> save(ContractTypePo contractTypePo);


}
