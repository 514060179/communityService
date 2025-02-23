package com.newland.property.store.bmo.contractPartya;

import com.newland.property.po.contract.ContractPartyaPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractPartyaBMO {


    /**
     * 修改合同房屋
     * add by wuxw
     *
     * @param contractPartyaPo
     * @return
     */
    ResponseEntity<String> delete(ContractPartyaPo contractPartyaPo);


}
