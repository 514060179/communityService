package com.newland.property.common.bmo.machineTranslateError.impl;

import com.newland.property.common.bmo.machineTranslateError.IGetMachineTranslateErrorBMO;
import com.newland.property.dto.machine.MachineTranslateErrorDto;
import com.newland.property.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getMachineTranslateErrorBMOImpl")
public class GetMachineTranslateErrorBMOImpl implements IGetMachineTranslateErrorBMO {

    @Autowired
    private IMachineTranslateErrorInnerServiceSMO machineTranslateErrorInnerServiceSMOImpl;

    /**
     * @param machineTranslateErrorDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(MachineTranslateErrorDto machineTranslateErrorDto) {


        int count = machineTranslateErrorInnerServiceSMOImpl.queryMachineTranslateErrorsCount(machineTranslateErrorDto);

        List<MachineTranslateErrorDto> machineTranslateErrorDtos = null;
        if (count > 0) {
            machineTranslateErrorDtos = machineTranslateErrorInnerServiceSMOImpl.queryMachineTranslateErrors(machineTranslateErrorDto);
        } else {
            machineTranslateErrorDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) machineTranslateErrorDto.getRow()), count, machineTranslateErrorDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
