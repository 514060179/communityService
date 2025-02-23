package com.newland.property.store.bmo.contract;

import com.newland.property.po.contract.ContractPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractBMO {


    /**
     * 修改合同管理
     * add by wuxw
     *
     * @param contractPo
     * @return
     */
    ResponseEntity<String> delete(ContractPo contractPo);


}
