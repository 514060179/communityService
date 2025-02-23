package com.newland.property.store.bmo.contractPartya;
import com.newland.property.po.contract.ContractPartyaPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractPartyaBMO {


    /**
     * 修改合同房屋
     * add by wuxw
     * @param contractPartyaPo
     * @return
     */
    ResponseEntity<String> update(ContractPartyaPo contractPartyaPo);


}
