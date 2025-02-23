package com.newland.property.store.bmo.contractTypeTemplate;

import com.newland.property.dto.contract.ContractDto;
import com.newland.property.dto.contract.ContractTypeSpecDto;
import com.newland.property.dto.contract.ContractTypeTemplateDto;
import org.springframework.http.ResponseEntity;

public interface IPrintContractTemplateBMO {


    /**
     * 查询合同属性及模板
     * add by wuxw
     *
     * @param contractTypeTemplateDto
     * @return
     */
    ResponseEntity<String> get(ContractTypeTemplateDto contractTypeTemplateDto, ContractDto contractDto, ContractTypeSpecDto contractTypeSpecDto);


}
