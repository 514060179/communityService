package com.newland.property.store.bmo.contractChangePlanRoom.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanRoomPo;
import com.newland.property.store.bmo.contractChangePlanRoom.IUpdateContractChangePlanRoomBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateContractChangePlanRoomBMOImpl")
public class UpdateContractChangePlanRoomBMOImpl implements IUpdateContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     * @param contractChangePlanRoomPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ContractChangePlanRoomPo contractChangePlanRoomPo) {

        int flag = contractChangePlanRoomInnerServiceSMOImpl.updateContractChangePlanRoom(contractChangePlanRoomPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
