package com.newland.property.store.bmo.contractChangePlanDetail.impl;

import com.newland.property.dto.contract.ContractChangePlanDetailDto;
import com.newland.property.intf.store.IContractChangePlanDetailInnerServiceSMO;
import com.newland.property.store.bmo.contractChangePlanDetail.IGetContractChangePlanDetailBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractChangePlanDetailBMOImpl")
public class GetContractChangePlanDetailBMOImpl implements IGetContractChangePlanDetailBMO {

    @Autowired
    private IContractChangePlanDetailInnerServiceSMO contractChangePlanDetailInnerServiceSMOImpl;

    /**
     * @param contractChangePlanDetailDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ContractChangePlanDetailDto contractChangePlanDetailDto) {


        int count = contractChangePlanDetailInnerServiceSMOImpl.queryContractChangePlanDetailsCount(contractChangePlanDetailDto);

        List<ContractChangePlanDetailDto> contractChangePlanDetailDtos = null;
        if (count > 0) {
            contractChangePlanDetailDtos = contractChangePlanDetailInnerServiceSMOImpl.queryContractChangePlanDetails(contractChangePlanDetailDto);
        } else {
            contractChangePlanDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractChangePlanDetailDto.getRow()), count, contractChangePlanDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
