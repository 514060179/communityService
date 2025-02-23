package com.newland.property.store.bmo.contractChangePlanRoom.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.newland.property.po.contract.ContractChangePlanRoomPo;
import com.newland.property.store.bmo.contractChangePlanRoom.ISaveContractChangePlanRoomBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractChangePlanRoomBMOImpl")
public class SaveContractChangePlanRoomBMOImpl implements ISaveContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractChangePlanRoomPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractChangePlanRoomPo contractChangePlanRoomPo) {

        contractChangePlanRoomPo.setPrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_prId));
        int flag = contractChangePlanRoomInnerServiceSMOImpl.saveContractChangePlanRoom(contractChangePlanRoomPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
