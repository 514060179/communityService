package com.newland.property.store.bmo.contractTypeTemplate;

import com.newland.property.po.contract.ContractTypeTemplatePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractTypeTemplateBMO {


    /**
     * 修改合同属性
     * add by wuxw
     *
     * @param contractTypeTemplatePo
     * @return
     */
    ResponseEntity<String> delete(ContractTypeTemplatePo contractTypeTemplatePo);


}
