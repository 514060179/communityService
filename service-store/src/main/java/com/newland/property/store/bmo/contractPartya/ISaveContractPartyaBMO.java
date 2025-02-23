package com.newland.property.store.bmo.contractPartya;

import com.newland.property.po.contract.ContractPartyaPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractPartyaBMO {


    /**
     * 添加合同房屋
     * add by wuxw
     * @param contractPartyaPo
     * @return
     */
    ResponseEntity<String> save(ContractPartyaPo contractPartyaPo);


}
