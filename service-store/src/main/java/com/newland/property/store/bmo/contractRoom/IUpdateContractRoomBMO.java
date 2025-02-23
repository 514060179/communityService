package com.newland.property.store.bmo.contractRoom;
import com.newland.property.po.contract.ContractRoomPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateContractRoomBMO {


    /**
     * 修改合同房屋
     * add by wuxw
     * @param contractRoomPo
     * @return
     */
    ResponseEntity<String> update(ContractRoomPo contractRoomPo);


}
