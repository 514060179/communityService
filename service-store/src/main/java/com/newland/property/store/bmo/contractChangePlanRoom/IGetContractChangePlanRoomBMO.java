package com.newland.property.store.bmo.contractChangePlanRoom;
import com.newland.property.dto.contract.ContractChangePlanRoomDto;
import org.springframework.http.ResponseEntity;
public interface IGetContractChangePlanRoomBMO {


    /**
     * 查询合同房屋变更
     * add by wuxw
     * @param  contractChangePlanRoomDto
     * @return
     */
    ResponseEntity<String> get(ContractChangePlanRoomDto contractChangePlanRoomDto);


}
