package com.newland.property.store.bmo.contractFile;
import com.newland.property.dto.contract.ContractFileDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractFileBMO {


    /**
     * 查询合同附件
     * add by wuxw
     * @param  contractFileDto
     * @return
     */
    ResponseEntity<String> get(ContractFileDto contractFileDto);


}
