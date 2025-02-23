package com.newland.property.store.bmo.contractChangePlanRoom.impl;

import com.newland.property.dto.contract.ContractChangePlanRoomDto;
import com.newland.property.intf.store.IContractChangePlanRoomInnerServiceSMO;
import com.newland.property.store.bmo.contractChangePlanRoom.IGetContractChangePlanRoomBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractChangePlanRoomBMOImpl")
public class GetContractChangePlanRoomBMOImpl implements IGetContractChangePlanRoomBMO {

    @Autowired
    private IContractChangePlanRoomInnerServiceSMO contractChangePlanRoomInnerServiceSMOImpl;

    /**
     * @param contractChangePlanRoomDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ContractChangePlanRoomDto contractChangePlanRoomDto) {


        int count = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRoomsCount(contractChangePlanRoomDto);

        List<ContractChangePlanRoomDto> contractChangePlanRoomDtos = null;
        if (count > 0) {
            contractChangePlanRoomDtos = contractChangePlanRoomInnerServiceSMOImpl.queryContractChangePlanRooms(contractChangePlanRoomDto);
        } else {
            contractChangePlanRoomDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractChangePlanRoomDto.getRow()), count, contractChangePlanRoomDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
