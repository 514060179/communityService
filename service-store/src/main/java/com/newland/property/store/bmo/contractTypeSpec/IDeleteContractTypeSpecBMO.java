package com.newland.property.store.bmo.contractTypeSpec;

import com.newland.property.po.contract.ContractTypeSpecPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractTypeSpecBMO {


    /**
     * 修改合同类型规格
     * add by wuxw
     *
     * @param contractTypeSpecPo
     * @return
     */
    ResponseEntity<String> delete(ContractTypeSpecPo contractTypeSpecPo);


}
