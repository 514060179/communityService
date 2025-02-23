package com.newland.property.store.bmo.contractRoom.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.store.IContractRoomInnerServiceSMO;
import com.newland.property.po.contract.ContractRoomPo;
import com.newland.property.store.bmo.contractRoom.IDeleteContractRoomBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteContractRoomBMOImpl")
public class DeleteContractRoomBMOImpl implements IDeleteContractRoomBMO {

    @Autowired
    private IContractRoomInnerServiceSMO contractRoomInnerServiceSMOImpl;

    /**
     * @param contractRoomPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ContractRoomPo contractRoomPo) {

        int flag = contractRoomInnerServiceSMOImpl.deleteContractRoom(contractRoomPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
