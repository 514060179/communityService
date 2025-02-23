package com.newland.property.store.bmo.contractType;

import com.newland.property.dto.contract.ContractTypeDto;
import org.springframework.http.ResponseEntity;

public interface IGetContractTypeBMO {


    /**
     * 查询合同类型
     * add by wuxw
     *
     * @param contractTypeDto
     * @return
     */
    ResponseEntity<String> get(ContractTypeDto contractTypeDto);


}
