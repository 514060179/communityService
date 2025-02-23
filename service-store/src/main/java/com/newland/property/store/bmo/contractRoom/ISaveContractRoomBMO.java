package com.newland.property.store.bmo.contractRoom;

import com.newland.property.po.contract.ContractRoomPo;
import org.springframework.http.ResponseEntity;
public interface ISaveContractRoomBMO {


    /**
     * 添加合同房屋
     * add by wuxw
     * @param contractRoomPo
     * @return
     */
    ResponseEntity<String> save(ContractRoomPo contractRoomPo);


}
