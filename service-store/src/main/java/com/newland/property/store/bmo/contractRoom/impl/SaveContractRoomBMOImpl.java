package com.newland.property.store.bmo.contractRoom.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IContractRoomInnerServiceSMO;
import com.newland.property.po.contract.ContractRoomPo;
import com.newland.property.store.bmo.contractRoom.ISaveContractRoomBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveContractRoomBMOImpl")
public class SaveContractRoomBMOImpl implements ISaveContractRoomBMO {

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param contractRoomPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ContractRoomPo contractRoomPo) {

        contractRoomPo.setCrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_crId));
        int flag = contractRoomInnerServiceSMOImpl.saveContractRoom(contractRoomPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
