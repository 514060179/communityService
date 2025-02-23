package com.newland.property.store.bmo.contractType;
import com.newland.property.po.contract.ContractTypePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractTypeBMO {


    /**
     * 修改合同类型
     * add by wuxw
     * @param contractTypePo
     * @return
     */
    ResponseEntity<String> delete(ContractTypePo contractTypePo);


}
