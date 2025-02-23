package com.newland.property.store.bmo.contractFile;
import com.newland.property.po.contract.ContractFilePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractFileBMO {


    /**
     * 修改合同附件
     * add by wuxw
     * @param contractFilePo
     * @return
     */
    ResponseEntity<String> delete(ContractFilePo contractFilePo);


}
