package com.newland.property.store.bmo.contractAttr;

import com.newland.property.dto.contract.ContractAttrDto;
import org.springframework.http.ResponseEntity;

public interface IGetContractAttrBMO {


    /**
     * 查询合同属性
     * add by wuxw
     *
     * @param contractAttrDto
     * @return
     */
    ResponseEntity<String> get(ContractAttrDto contractAttrDto);


}
