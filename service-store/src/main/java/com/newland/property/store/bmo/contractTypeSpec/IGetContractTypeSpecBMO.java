package com.newland.property.store.bmo.contractTypeSpec;

import com.newland.property.dto.contract.ContractTypeSpecDto;
import org.springframework.http.ResponseEntity;

public interface IGetContractTypeSpecBMO {


    /**
     * 查询合同类型规格
     * add by wuxw
     *
     * @param contractTypeSpecDto
     * @return
     */
    ResponseEntity<String> get(ContractTypeSpecDto contractTypeSpecDto);


}
