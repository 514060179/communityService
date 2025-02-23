package com.newland.property.store.bmo.contractChangePlanRoom;
import com.newland.property.po.contract.ContractChangePlanRoomPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteContractChangePlanRoomBMO {


    /**
     * 修改合同房屋变更
     * add by wuxw
     * @param contractChangePlanRoomPo
     * @return
     */
    ResponseEntity<String> delete(ContractChangePlanRoomPo contractChangePlanRoomPo);


}
