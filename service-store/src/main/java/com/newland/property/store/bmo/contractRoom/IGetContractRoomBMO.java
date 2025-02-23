package com.newland.property.store.bmo.contractRoom;
import com.newland.property.dto.contract.ContractRoomDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractRoomBMO {


    /**
     * 查询合同房屋
     * add by wuxw
     * @param  contractRoomDto
     * @return
     */
    ResponseEntity<String> get(ContractRoomDto contractRoomDto);


}
