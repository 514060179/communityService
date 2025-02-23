package com.newland.property.store.bmo.contractAttr;

import com.newland.property.po.contract.ContractAttrPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractAttrBMO {


    /**
     * 修改合同属性
     * add by wuxw
     *
     * @param contractAttrPo
     * @return
     */
    ResponseEntity<String> update(ContractAttrPo contractAttrPo);


}
